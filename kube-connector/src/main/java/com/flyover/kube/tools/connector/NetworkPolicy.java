package com.flyover.kube.tools.connector;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.NetworkPolicyEgressRuleModel;
import com.flyover.kube.tools.connector.model.NetworkPolicyIngressRuleModel;
import com.flyover.kube.tools.connector.model.NetworkPolicyIngressRuleModel.Port;
import com.flyover.kube.tools.connector.model.NetworkPolicyModel;
import com.flyover.kube.tools.connector.model.NetworkPolicySpecModel;
import com.flyover.kube.tools.connector.model.SelectorModel;

public class NetworkPolicy {

    private Kubernetes kube;
    private NetworkPolicyModel model;

    public NetworkPolicy(Kubernetes kube) {
        this(kube, new NetworkPolicyModel());
    }

    public NetworkPolicy(Kubernetes kube, NetworkPolicyModel model) {
        this.kube = kube;
        this.model = model;
    }

    public KubeMetadataModel metadata() {
        return this.model.getMetadata();
    }

    public NetworkPolicySpec spec() { return new NetworkPolicySpec(this.model.getSpec()); }

    public NetworkPolicyModel model() { return this.model; }

    public NetworkPolicy find() {
        
    	this.model = kube.find(this.model);

        if (this.model == null) {
            return null;
        }
        
        return this;
        
    }

	public NetworkPolicy merge() {
		
		return merge(new Callback<NetworkPolicyModel>() {});
		
	}
	
	public NetworkPolicy merge(Callback<NetworkPolicyModel> c) {
		
		NetworkPolicyModel found = kube.find(this.model);
		
		if(found == null) {
			this.model = kube.create(this.model, c);
		} else {
			this.model = kube.update(found, this.model, c);
		}
		
		return this;
		
	}

    public void delete() {
        kube.delete(this.model);
    }

    public static class NetworkPolicySpec {
    	
        private NetworkPolicySpecModel model;

        public NetworkPolicySpec(NetworkPolicySpecModel model) { 
        	this.model = model; 
        }

        public PodSelector podSelector() {
        	
        	if(this.model.getPodSelector() == null) {
        		this.model.setPodSelector(new SelectorModel());
        	}
        	
        	return new PodSelector(this.model.getPodSelector());
        	
        }

        public Ingress ingress() {
        	
        	this.model.getPolicyTypes().add("Ingress");
        	this.model.setIngress(new LinkedList<>());
        	
        	return new Ingress(this.model.getIngress());
        	
        }
        
        public Egress egress() {
        	
        	this.model.getPolicyTypes().add("Egress");
        	this.model.setEgress(new LinkedList<>());
        	
        	return new Egress(this.model.getEgress());
        	
        }

    }
    
    public static class PodSelector {
    	
    	private SelectorModel model;
    	
    	public PodSelector(SelectorModel model) {
			this.model = model;
		}

		public Map<String, String> matchLabels() {
    		return model.getMatchLabels();
    	}
    	
    }
    
    public static class Ingress {
    	
    	private List<NetworkPolicyIngressRuleModel> model;

		public Ingress(List<NetworkPolicyIngressRuleModel> model) {
			this.model = model;
		}
    	
		public IngressRule from(FromType...types) {
			
			NetworkPolicyIngressRuleModel rule = new NetworkPolicyIngressRuleModel();
			
			Arrays.asList(types).forEach(t -> {
				rule.getFrom().add(t.model());
			});
			
			this.model.add(rule);
			
			return new IngressRule(rule);
			
		}
		
		public static NamespaceSelector namespaceSelector() {
			return new NamespaceSelector();
		}
		
		public static AllPodsInNamespace allPodsInNamespace() {
			return new AllPodsInNamespace();
		}
		
		public static FromIPBlock ipBlock() {
			return new FromIPBlock();
		}
		
    }
    
    public static class Egress {
    	
    	private List<NetworkPolicyEgressRuleModel> model;

		public Egress(List<NetworkPolicyEgressRuleModel> model) {
			this.model = model;
		}
    	
		public EgressRule to(ToType...types) {
			
			NetworkPolicyEgressRuleModel rule = new NetworkPolicyEgressRuleModel();
			
			Arrays.asList(types).forEach(t -> {
				rule.getTo().add(t.model());
			});
			
			this.model.add(rule);
			
			return new EgressRule(rule);
			
		}
		
		public static NamespaceSelector namespaceSelector() {
			return new NamespaceSelector();
		}
		
		public static ToAllPodsInNamespace allPodsInNamespace() {
			return new ToAllPodsInNamespace();
		}
		
		public static IPBlock ipBlock() {
			return new IPBlock();
		}
		
    }
    
    public static class IngressRule {
    	
    	private NetworkPolicyIngressRuleModel model;

		public IngressRule(NetworkPolicyIngressRuleModel model) {
			this.model = model;
		}
    	
		public IngressRule port(String protocol, int port) {
			
			Port p = new Port();
			p.setProtocol(protocol);
			p.setPort(port);
			
			if(model.getPorts() == null) {
				model.setPorts(new LinkedList<>());
			}
			
			model.getPorts().add(p);
			
			return this;
			
		}
		
    }
    
    public static interface FromType {
    	
    	Map<String, Object> model();
    	
    }
    
    public static class NamespaceSelector implements FromType {
    	
    	private Map<String, Object> labels = new LinkedHashMap<String, Object>();

    	public NamespaceSelector label(String name, String value) {
			
    		labels.put(name, value);
    		
    		return this;
    		
		}
    	
		@Override
		public Map<String, Object> model() {
			
			return Collections.singletonMap("namespaceSelector", 
					Collections.singletonMap("matchLabels", labels));
			
		}
    	
    }
    
    public static class AllPodsInNamespace implements FromType {
    	
		@Override
		public Map<String, Object> model() {
			
			return Collections.singletonMap("podSelector", Collections.emptyMap());
			
		}
    	
    }
    
    public static class EgressRule {
    	
    	private NetworkPolicyEgressRuleModel model;

		public EgressRule(NetworkPolicyEgressRuleModel model) {
			this.model = model;
		}
    	
		public EgressRule port(String protocol, int port) {
			
			NetworkPolicyEgressRuleModel.Port p = new NetworkPolicyEgressRuleModel.Port();
			p.setProtocol(protocol);
			p.setPort(port);
			
			if(model.getPorts() == null) {
				model.setPorts(new LinkedList<>());
			}
			
			model.getPorts().add(p);
			
			return this;
			
		}
		
    }
    
    public static interface ToType {
    	
    	Map<String, Object> model();
    	
    }
    
    public static class ToAllPodsInNamespace implements ToType {
    	
		@Override
		public Map<String, Object> model() {
			
			return Collections.singletonMap("podSelector", Collections.emptyMap());
			
		}
    	
    }
    
    public static class IPBlock implements ToType {
    	
    	private String cidr;
    	private List<String> except = new LinkedList<>();
    	
    	public IPBlock cidr(String cidr) {
    		this.cidr = cidr;
    		return this;
		}
    	
    	public IPBlock except(String...cidr) {
    		this.except.addAll(Arrays.asList(cidr));
    		return this;
		}
    	
		@Override
		public Map<String, Object> model() {
			
			Map<String, Object> value = new LinkedHashMap<>();
			value.put("cidr", cidr);
			value.put("except", except);
			
			return Collections.singletonMap("ipBlock", value);
			
		}
    	
    }
    
    public static class FromIPBlock implements FromType {
    	
    	private String cidr;
    	private List<String> except = new LinkedList<>();
    	
    	public FromIPBlock cidr(String cidr) {
    		this.cidr = cidr;
    		return this;
		}
    	
    	public FromIPBlock except(String...cidr) {
    		this.except.addAll(Arrays.asList(cidr));
    		return this;
		}
    	
		@Override
		public Map<String, Object> model() {
			
			Map<String, Object> value = new LinkedHashMap<>();
			value.put("cidr", cidr);
			value.put("except", except);
			
			return Collections.singletonMap("ipBlock", value);
			
		}
    	
    }

}
