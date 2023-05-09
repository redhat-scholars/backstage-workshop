package com.rhdevelopers.summit.workshop.k8s;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.quarkus.logging.Log;

public abstract class AbstractResourceWatcher<T extends HasMetadata> implements Watcher<T> {

    public static final String PROTOCOL_HTTP = "http://";
    public static final String PROTOCOL_HTTPS = "https://";
    public static final String PORT_SEPARATOR = ":";
    public static final int DEFAULT_PORT = 8080;

    @Override
    public void eventReceived(Action action, T resource) {
        var resourceName = resource.getMetadata().getName();
        switch (action) {
            case ADDED, MODIFIED, DELETED -> {
                Log.infov("handle resource: {0} with event {1}", resourceName, action);
                processResourceEvent(action, resource);
            }
            default -> Log.infov("ignore resource: {0} with event {1}", resourceName, action);
        }
    }

    @Override
    public void onClose() {
        Watcher.super.onClose();
    }

    @Override
    public void onClose(WatcherException cause) {}

    @Override
    public boolean reconnecting() {
        return Watcher.super.reconnecting();
    }

    abstract void processResourceEvent(Action action, T resource);
    
    protected boolean hasQualifyingLabel(T resource, String key, String value) {
        var labels = resource.getMetadata().getLabels();
        Log.debugv("resource has the following label attached: {0}",labels);
        Log.debugv("checking for resource label: {0}={1}",key,value);
        return labels.getOrDefault(key, "").equals(value);
    }

    protected String getName(T resource) {
        return resource.getMetadata().getName();
    }

}
