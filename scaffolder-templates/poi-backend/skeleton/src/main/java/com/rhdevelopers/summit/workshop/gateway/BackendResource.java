package com.rhdevelopers.summit.workshop.gateway;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Path("/ws")
public class BackendResource {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @ConfigProperty(name = "poi.backend.resource.info")
    String poiBackendResourceInfo;

    private Backend backend;

    public static record Coordinates(double lat, double lng) {};

    public static record Backend(
        String id,
        String displayName,
        Coordinates coordinates,
        int zoom
    ) {};

    @PostConstruct
    void initBackend() {
        try {
            backend = OBJECT_MAPPER.readValue(poiBackendResourceInfo,Backend.class);
        } catch (JsonProcessingException e) {
            Log.error("error parsing configured backend info from configuration");
            Log.error(e.getMessage());
        }
    }

    @GET
    @Path("info")
    @APIResponse(
            responseCode = "200",
            description = "get information about this registered backend",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Backend.class)
            )
    )
    public Response getBackend() {
        Log.info("backend info endpoint called");
        Log.debugv("returning backend {0}",backend);
        return Response.ok(backend).build();
    }

    @GET
    @Path("healthz")
    @APIResponse(
            responseCode = "200",
            description = "artificial legacy health endpoint for liveness and readiness probes",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Response getHealth() {
        return Response.ok("OK").build();
    }

}
