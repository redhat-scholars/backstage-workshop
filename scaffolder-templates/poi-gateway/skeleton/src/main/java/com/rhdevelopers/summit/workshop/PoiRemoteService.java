package com.rhdevelopers.summit.workshop;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

public interface PoiRemoteService {

    @GET
    @Path("ws/info")
    public Response getInfo();

    @GET
    @Path("poi/find/all")
    public Response getAllData();

    static PoiRemoteService createRestClient(String resourceEndpoint) {
        try {
            return RestClientBuilder.newBuilder()
                .baseUri(new URI(resourceEndpoint))
                .build(PoiRemoteService.class);
        } catch (Exception e) {
            throw new RuntimeException("rest client creation failed for resource endpoint "+resourceEndpoint,e);
        }
    }

}
