package com.rhdevelopers.summit.workshop.k8s;

import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.rhdevelopers.summit.workshop.BackendRegistry;

import io.fabric8.kubernetes.api.model.Service;
import io.quarkus.logging.Log;

@Singleton
public class ServiceWatcher extends AbstractResourceWatcher<Service> {

    @ConfigProperty(name="watcher.service.label.key",defaultValue="type")
    String watcherServiceLabelKey;

    @ConfigProperty(name="watcher.service.label.value",defaultValue="map-backend")
    String watcherServiceLabelValue;
    
    private BackendRegistry backendRegistry;

    public ServiceWatcher(BackendRegistry backendRegistry) {
        this.backendRegistry = backendRegistry;
    }

    @Override
    void processResourceEvent(Action action, Service resource) {
        
        var serviceName = getName(resource);
        var serviceURL = getServiceURL(resource);
        
        switch (action) {
            case ADDED -> {
                if (hasQualifyingLabel(resource,watcherServiceLabelKey,watcherServiceLabelValue)) {
                    registerService(serviceName, serviceURL);
                } else {
                    Log.debug("ignore service due to missing label of interest");
                }
            }
            case MODIFIED -> {
                if (hasQualifyingLabel(resource,watcherServiceLabelKey,watcherServiceLabelValue)) {
                    registerService(serviceName, serviceURL);
                } else {
                    unregisterService(serviceName, serviceURL);
                }
            }
            case DELETED, ERROR -> unregisterService(serviceName, serviceURL);
            default -> Log.debugv("action {0} for service {1} ignored", action, serviceName);
        }
    }

    private void registerService(String serviceName, String serviceURL) {
        Log.debugv("handle service with matching label of interest");
        Log.debugv("derived service URL -> {0}", serviceURL);
        backendRegistry.register(serviceName, serviceURL);
    }

    private void unregisterService(String serviceName, String serviceURL) {
        Log.debugv("derived service URL -> {0}", serviceURL);
        Log.debugv("trying to unregister potentially registered service due to service deletion or qualified label removal");
        backendRegistry.unregister(serviceName, serviceURL);
    }

    private String getServiceURL(Service resource) {
        var servicePorts = resource.getSpec().getPorts();
        var port = servicePorts.isEmpty() ? DEFAULT_PORT : servicePorts.get(0).getPort();
        return PROTOCOL_HTTP + getName(resource) + PORT_SEPARATOR + port;
    }

}
