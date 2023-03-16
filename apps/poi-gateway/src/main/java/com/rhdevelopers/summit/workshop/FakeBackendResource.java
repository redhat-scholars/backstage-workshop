package com.rhdevelopers.summit.workshop;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhdevelopers.summit.workshop.BackendRegistry.Backend;
import com.rhdevelopers.summit.workshop.BackendRegistry.Coordinates;

@Path("fake/")
public class FakeBackendResource {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final Backend FAKE_BACKEND_INFO = new Backend(
        "fake-poi-backend",
        "Fake POI Backend",
        new Coordinates(0.0, 0.0),
        4
    );
    
    public static final List<PoiRecord> FAKE_POI_RECORDS = List.of(
        new PoiRecord("Red Hat Headquarters", "Raleigh,NC,USA", List.of(35.787743, -78.644257), null, null),
        new PoiRecord("Red Hat Summit","Bosten,MA,USA", List.of(42.361145, -71.057083), null, null)
    );

    @GET
    @Path("ws/info")
    public Response getInfo() throws IOException {
        return Response.ok(
            OBJECT_MAPPER.writeValueAsString(FAKE_BACKEND_INFO),
            MediaType.APPLICATION_JSON
        ).build();
    }

    @GET
    @Path("ws/data/all")
    public Response getAllDataLegacyFacade() throws IOException {
        return Response.ok(
            OBJECT_MAPPER.writeValueAsString(FAKE_POI_RECORDS),
            MediaType.APPLICATION_JSON
        ).build();
    }

    @GET
    @Path("poi/find/all")
    public Response getAllData() throws IOException {
        return Response.ok(
            OBJECT_MAPPER.writeValueAsString(FAKE_POI_RECORDS),
            MediaType.APPLICATION_JSON
        ).build();
    }

}
