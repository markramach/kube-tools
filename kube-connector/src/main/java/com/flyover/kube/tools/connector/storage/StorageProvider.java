package com.flyover.kube.tools.connector.storage;

import com.flyover.kube.tools.connector.Kubernetes;
import com.flyover.kube.tools.connector.Volume;

public interface StorageProvider {

	Volume build(Kubernetes kube, String namespace, String name, String alias);

}