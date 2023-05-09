package com.rhdevelopers.summit.workshop.park.facade.legacy;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import com.rhdevelopers.summit.workshop.poi.PoiService;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.logging.Log;

@Path("ws/data")
public class ParkResource {

    @Inject
    PoiService poiService;

    @GET
    @Path("load")
    @APIResponse(
            responseCode = "200",
            description = "preset parks data loaded",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Response loadParks() {
        var count = poiService.insertPresetPoiRecordsOfTypePark();
        return Response.ok("Items inserted in database: "+count).build();
    }

    @GET
    @Path("all")
    @APIResponse(
            responseCode = "200",
            description = "get all parks",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = ParkRecord.class)
            )
    )
    public Response findAllParks() {
        var parks = poiService.findAllPoisByType("park").stream()
                        .map(r -> ParkRecord.fromPoiRecord(r))
                        .collect(Collectors.toList());
        Log.debugv("returning {0} parks",parks.size());
        return Response.ok(parks).build();
    }


    @GET
    @Path("within")
    @APIResponse(
            responseCode = "200",
            description = "get all parks within a geo region",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = ParkRecord.class)
            )
    )
    public Response findParksWithinRegion() {
        return Response.serverError().entity("upps... not yet implemented!").build();
    }

}
