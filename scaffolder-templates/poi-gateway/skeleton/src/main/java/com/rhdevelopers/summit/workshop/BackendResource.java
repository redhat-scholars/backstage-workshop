package com.rhdevelopers.summit.workshop;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.rhdevelopers.summit.workshop.BackendRegistry.Backend;

@Path("/backend")
public class BackendResource {

    @Inject
    BackendRegistry backendRegistry;

    @GET
    @Path("list")
    public List<Backend> getAllBackends() {
        return backendRegistry.getAllRegisteredBackends()
            .entrySet().stream()
            .map(e -> e.getValue().backendInfo())
            .collect(Collectors.toList());
    }

    @GET
    @Path("unregister/{backendId}")
    public Response unregisterBackend(@PathParam("backendId") String backendId) {
        backendRegistry.unregister(backendId, "");
        return Response.noContent().build();
    }

    @GET
    @Path("register/{backendId}")
    public Response registerBackend(@PathParam("backendId") String backendId, @QueryParam("endpoint") String endpoint) {
        backendRegistry.register(backendId, endpoint);
        return Response.noContent().build();
    }
   
}
