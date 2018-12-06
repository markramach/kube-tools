/**
 * 
 */
package com.flyover.kube.tools.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.flyover.kube.tools.connector.storage.model.ConfigMapVolumeModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flyover.kube.tools.connector.model.GenericKubeItemsModel;
import com.flyover.kube.tools.connector.model.KubeModel;
import com.flyover.kube.tools.connector.model.PathsModel;
import com.flyover.kube.tools.connector.model.PodModel;
import com.flyover.kube.tools.connector.model.ResourceListModel;
import com.flyover.kube.tools.connector.model.ResourceModel;
import com.flyover.kube.tools.connector.model.VersionModel;
import com.flyover.kube.tools.connector.storage.model.EmptyDirVolumeModel;

/**
 * @author mramach
 *
 */
public class Kubernetes {
	
	private ExecutorService pool = Executors.newFixedThreadPool(4);
	private OkHttpClient client = new OkHttpClient();
	private KubernetesConfig config;
	private RestTemplate restTemplate;

	public Kubernetes() {
		this(new KubernetesConfig());
	}
	
	public Kubernetes(KubernetesConfig config) {
		
		RestTemplate restTemplate = new RestTemplate();
		// apply ssl policy
		config.getSslPolicy().configure(restTemplate);
		// apply authenticator
		config.getAuthenticator().configure(restTemplate);
		
		this.setRestTemplate(restTemplate);
		this.setConfig(config);
		
	}

	public void close() {
		
		pool.shutdown();
		
		ExecutorService clientPool = client.dispatcher().executorService();
		clientPool.shutdown();
		
		try {
		
			pool.awaitTermination(60, TimeUnit.SECONDS);
			clientPool.awaitTermination(60, TimeUnit.SECONDS);
			
		} catch (InterruptedException e) {
			// log this
		}
		
	}
	
	public KubernetesConfig getConfig() {
		return config;
	}

	public void setConfig(KubernetesConfig config) {
		this.config = config;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public VersionModel version() {
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getEndpoint())
				.path("/version");
		
		return restTemplate.getForObject(builder.build().toUri(), VersionModel.class);
		
	}

	public Namespace namespace(String name) {
		
		Namespace ns = new Namespace(this);
		ns.metadata().setName(name);
		
		return ns;
		
	}
	
	public Volume volume(String namespace, String name, String alias) {
		
		return config.getStorageProvider().build(this, namespace, name, alias);
		
	}
	
	public CustomResourceDefinition crd(String name) {
		
		CustomResourceDefinition crd = new CustomResourceDefinition(this);
		crd.metadata().setName(name);
		
		return crd;
		
	}
	
	public Volume emptyDir(String alias) {
		
		EmptyDirVolumeModel model = new EmptyDirVolumeModel();
		model.setName(alias);
		
		return new Volume(model);
		
	}
	
	public <T extends KubeModel> T create(T model) {

		ResourceRef ref = resource(model);
		
		if(ref.getResource().isNamespaced()) {
			
			URI uri = UriComponentsBuilder
    			.fromHttpUrl(config.getEndpoint())
    			.path(ref.getPath())
    			.path("/namespaces/")
    			.path(model.getMetadata().getNamespace())
    			.path("/")
    			.path(ref.getResource().getName())
    			.build()
    				.toUri();
			
			return create(uri, model);
			
		} else {
			
			URI uri = UriComponentsBuilder
    			.fromHttpUrl(config.getEndpoint())
    			.path(ref.getPath())
    			.path("/")
    			.path(ref.getResource().getName())
    			.build()
    				.toUri();
			
			return create(uri, model);
			
		}
		
	}
	
	public <T extends KubeModel> T update(T existing, T model) {

		return update(uri(model, resource(model)), existing, model);
		
	}
	
	public <T extends KubeModel> T find(T model) {

		return find(uri(model, resource(model)), model);
		
	}
	
	public <T extends KubeModel> void delete(T model) {
		
		delete(uri(model, resource(model)), model);
		
	}
	
	@SuppressWarnings("unchecked")
	public <T extends KubeModel> List<T> list(T model, Map<String, String> selectors) {
		
		return (List<T>) list(model.getClass(), uri(model, resource(model), true, false), selectors);
		
	}
	
	@SuppressWarnings("unchecked")
	public <T extends KubeModel> List<T> listAllNamespaces(T model, Map<String, String> selectors) {
		
		return (List<T>) list(model.getClass(), uri(model, resource(model), true, true), selectors);
		
	}
	
	public String exec(PodModel model, String command) {
		
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUri(uri(model, resource(model)))
				.path("/exec")
				.queryParam("command", command)
				.queryParam("stderr", true)
				.queryParam("stdout", true);
		
		Request request = new Request.Builder()
			.url(builder.build().toUri().toString())
			.header("X-Stream-Protocol-Version", "v4.channel.k8s.io")
			.header("X-Stream-Protocol-Version", "v3.channel.k8s.io")
			.header("X-Stream-Protocol-Version", "v2.channel.k8s.io")
			.header("X-Stream-Protocol-Version", "v.channel.k8s.io")
				.build();
		
		CompletableFuture<String> promise = new CompletableFuture<>();
		
		WebSocketListener listener = new WebSocketListener() {
			
			private ByteArrayOutputStream out = new ByteArrayOutputStream();

			@Override
			public void onMessage(WebSocket webSocket, String text) {
				
				try {
					out.write(text.getBytes());
				} catch (IOException e) {
					promise.completeExceptionally(e);
				}
				
			}

			@Override
			public void onMessage(WebSocket webSocket, ByteString bytes) {
				try {
					out.write(bytes.toByteArray());
				} catch (IOException e) {
					promise.completeExceptionally(e);
				}
			}

			@Override
			public void onClosing(WebSocket webSocket, int code, String reason) {
				promise.complete(new String(out.toByteArray()));
			}

			@Override
			public void onClosed(WebSocket webSocket, int code, String reason) {
				promise.complete(new String(out.toByteArray()));
			}

			@Override
			public void onFailure(WebSocket webSocket, Throwable t, Response response) {
				promise.completeExceptionally(t);
			}
			
		};
		
		WebSocket socket = null;
		
		try {
			
			socket = client.newWebSocket(request, listener);
			
			return promise.get();
			
		} catch (Exception e) {
			
			throw new RuntimeException("Failed during exec call.", e);
			
		} finally {
			
			if(socket != null) {
				socket.close(1000, null);
			}
			
		}
		
	}

	private <T extends KubeModel> URI uri(T model, ResourceRef ref) {
		
		return uri(model, ref, false, false);
		
	}
	
	private <T extends KubeModel> URI uri(T model, ResourceRef ref, boolean list, boolean allNamespaces) {
		
		URI uri = null;
		
		if(ref.getResource().isNamespaced()) {
			
			UriComponentsBuilder builder = UriComponentsBuilder
    			.fromHttpUrl(config.getEndpoint())
    			.path(ref.getPath());
			
				if(!allNamespaces) {
					
					builder.path("/namespaces/")
    					.path(model.getMetadata().getNamespace());
					
				}
    			
    			builder.path("/")
    				.path(ref.getResource().getName());
			
			if(!list) {
			
				builder = builder
					.path("/")
					.path(model.getMetadata().getName());
		    			
			}
    			
			uri = builder.build().toUri();
			
		} else {
			
			UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(config.getEndpoint())
				.path(ref.getPath())
				.path("/")
				.path(ref.getResource().getName());
			
			if(!list) {
				
				builder = builder
					.path("/")
					.path(model.getMetadata().getName());
		    			
			}
    			
			uri = builder.build().toUri();
			
		}
		
		return uri;
		
	}
	
	protected <T> Future<T> execute(Callable<T> task) {
		
		return this.pool.submit(task);
		
	}
	
	@SuppressWarnings("unchecked")
	private <T extends KubeModel> T create(URI uri, T model) {

		model.getMetadata().getAnnotations().put("com.flyover.checksum", model.checksum());
		
		return (T) restTemplate.postForObject(uri, model, model.getClass());
		
	}
	
	@SuppressWarnings("unchecked")
	private <T extends KubeModel> T update(URI uri, T existing, T model) {

		String checksum = existing.getMetadata().getAnnotations().getOrDefault("com.flyover.checksum", "");
		
		if(model.checksum().equals(checksum)) {
			return existing;
		}
		
		model.getMetadata().getAnnotations().put("com.flyover.checksum", model.checksum());
		
		existing.merge(model);
		
		return (T) restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(existing), model.getClass()).getBody();
		
	}
	
	@SuppressWarnings("unchecked")
	private <T extends KubeModel> T find(URI uri, T model) {

		try {
			
			return (T) restTemplate.getForObject(uri, model.getClass());
			
		} catch (HttpClientErrorException e) {
			
			if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
				return null;
			}
			
			throw e;
			
		}
		
	}
	
	private <T extends KubeModel> List<T> list(Class<T> type, URI uri, Map<String, String> selectors) {

		try {
			
			String labelSelector = selectors.entrySet().stream()
				.map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
				.collect(Collectors.joining(","));
			
			URI uriFull = UriComponentsBuilder.fromUri(uri)
				.queryParam("labelSelector", labelSelector)
					.build().toUri();
			
			GenericKubeItemsModel items = restTemplate
					.getForObject(uriFull, GenericKubeItemsModel.class);
			
			ObjectMapper mapper = new ObjectMapper();
			
			return items.getItems().stream()
				.map(i -> mapper.convertValue(i, type))
					.collect(Collectors.toList());
			
		} catch (HttpClientErrorException e) {
			
			if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
				return null;
			}
			
			throw e;
			
		}
		
	}
	
	private <T extends KubeModel> void delete(URI uri, T model) {

		try {
			
			restTemplate.delete(uri);
			
		} catch (HttpClientErrorException e) {
			
			if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
				return; // no action
			}
			
			throw e;
			
		}
		
	}
	
	private String path(KubeModel model) {
		
		// get the api path for the component
		
//		URI uri = UriComponentsBuilder
//			.fromHttpUrl(config.getEndpoint()).build().toUri();
//		
//		PathsModel res = restTemplate.getForObject(uri, PathsModel.class); 
		
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("kubernetes.paths.json");
		
		try {
			
			PathsModel res = new ObjectMapper().readValue(input, PathsModel.class);
			
			return res.getPaths().stream()
				.filter(p -> p.contains(model.getApiVersion()))
				.findFirst()
					.orElseThrow(() -> new RuntimeException(
						String.format("could not determine api path for compoent with apiVersion %s and kind", 
								model.getApiVersion(), model.getKind())));
			
		} catch (IOException e) {
			throw new RuntimeException("failed to load paths resource", e);
		}
		
	}
	
	private static final Map<Class<? extends KubeModel>, ResourceModel> RESOURCE_MODEL_CACHE = new LinkedHashMap<>();
	
	private ResourceRef resource(KubeModel model) {

		String path = path(model);
		
		if(RESOURCE_MODEL_CACHE.containsKey(model.getClass())) {
			return new ResourceRef(path, RESOURCE_MODEL_CACHE.get(model.getClass()));
		}
		
		URI uri = UriComponentsBuilder
			.fromHttpUrl(config.getEndpoint())
			.path(path)
			.build()
				.toUri();
		
		ResourceListModel resourceList = restTemplate.getForObject(uri, ResourceListModel.class);
		
		ResourceModel resource = resourceList.getResources().stream()
			.filter(r -> model.getKind().equals(r.getKind()))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(
					String.format("could not find api resource for compoent with apiVersion %s and kind %s", 
							model.getApiVersion(), model.getKind())));
		
		RESOURCE_MODEL_CACHE.put(model.getClass(), resource);
		
		return new ResourceRef(path, resource);
		
	}

	private static class ResourceRef {
		
		private String path;
		private ResourceModel resource;
		
		public ResourceRef(String path, ResourceModel resource) {
			this.path = path;
			this.resource = resource;
		}

		public String getPath() {
			return path;
		}

		public ResourceModel getResource() {
			return resource;
		}
		
	}

	public Volume createConfigMapVolume(String alias) {
		ConfigMapVolumeModel configMapVolumeModel = new ConfigMapVolumeModel();

		configMapVolumeModel.setName(alias);
		configMapVolumeModel.setDefaultMode(420);
		ConfigMapVolumeModel.KeyToPathModel keyToPathModel = new ConfigMapVolumeModel.KeyToPathModel();
		keyToPathModel.setKey("config");
		keyToPathModel.setPath(alias);
		configMapVolumeModel.setItems(Arrays.asList(keyToPathModel));

		return new Volume(configMapVolumeModel);
	}
	
}
