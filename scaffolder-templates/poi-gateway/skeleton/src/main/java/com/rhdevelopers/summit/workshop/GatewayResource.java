package com.rhdevelopers.summit.workshop;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.rhdevelopers.summit.workshop.k8s.ServiceWatcher;

import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.openshift.client.OpenShiftClient;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;

@Startup
@Path("/ws/data")
public class GatewayResource {

    @Inject
    OpenShiftClient k8sClient;

    @Inject
    BackendRegistry backendRegistry;

    @Inject
    ServiceWatcher serviceWatcher;

    @PostConstruct
    void init() {
        try {
            var namespace = k8sClient.getNamespace();
            Log.debugv("watching services via k8s API in namespace: {0}",namespace);
            k8sClient.services().inNamespace(namespace).watch(serviceWatcher);
        } catch(KubernetesClientException exc) {
            Log.error("failed to start watching services via k8s API which is most likely due to missing permissions");
            Log.error(exc.getMessage());
        }
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PoiRecord> findAll(@QueryParam("service") String backendId) {
        var backendProxy = backendRegistry.getBackendForId(backendId);
        if (backendProxy == null) {
            Log.errorv("no backend with id {0} found in registry",backendId);
            return null;
        }
        return backendProxy.restClient().getAllData().readEntity(new GenericType<List<PoiRecord>>() {});
    }

}