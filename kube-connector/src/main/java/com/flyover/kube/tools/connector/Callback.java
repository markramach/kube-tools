/**
 * 
 */
package com.flyover.kube.tools.connector;

import com.flyover.kube.tools.connector.model.KubeModel;

/**
 * @author mramach
 *
 */
public interface Callback<T extends KubeModel> {

	default void onCreate(T entity){
		// do nothing
	}
	
	default void onUpdate(T entity){
		// do nothing
	}
	
}
