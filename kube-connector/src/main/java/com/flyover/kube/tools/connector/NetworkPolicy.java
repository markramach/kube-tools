package com.flyover.kube.tools.connector;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.flyover.kube.tools.connector.model.KubeMetadataModel;
import com.flyover.kube.tools.connector.model.NetworkPolicyIngressRuleModel;
import com.flyover.kube.tools.connector.model.NetworkPolicyIngressRuleModel.Port;
import com.flyover.kube.tools.connector.model.IngressModel;
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
        	
        	this.model.setIngress(new LinkedList<>());
        	
        	return new Ingress(this.model.getIngress());
        	
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
    	
		public Rule from(FromType...types) {
			
			NetworkPolicyIngressRuleModel rule = new NetworkPolicyIngressRuleModel();
			
			Arrays.asList(types).forEach(t -> {
				rule.getFrom().add(t.model());
			});
			
			this.model.add(rule);
			
			return new Rule(rule);
			
		}
		
		public static NamespaceSelector namespaceSelector() {
			return new NamespaceSelector();
		}
		
		public static AllPodsInNamespace allPodsInNamespace() {
			return new AllPodsInNamespace();
		}
		
    }
    
    public static class Rule {
    	
    	private NetworkPolicyIngressRuleModel model;

		public Rule(NetworkPolicyIngressRuleModel model) {
			this.model = model;
		}
    	
		public Rule port(String protocol, int port) {
			
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

}
