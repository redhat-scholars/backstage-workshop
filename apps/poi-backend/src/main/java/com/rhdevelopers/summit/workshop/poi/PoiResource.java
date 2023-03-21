package com.rhdevelopers.summit.workshop.poi;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.logging.Log;

@Path("/poi")
@Produces(MediaType.APPLICATION_JSON)
public class PoiResource {

    @Inject
    PoiService poiService;

    @POST
    @Path("init/preset/park")
    @APIResponse(
            responseCode = "201",
            description = "preset points-of-interest for parks created",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Response initPresetNationalpark() {
        var count = poiService.insertPresetPoiRecordsOfTypePark();
        return Response.created(UriBuilder.fromPath("poi/find/park/all").build())
                .entity("inserted "+count+" preset poi record(s) for type 'park'")
                .build();
    }

    @POST
    @Path("init/custom/{poi-type}")
    @APIResponse(
            responseCode = "201",
            description = "custom points-of-interest created",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Response loadCustomPoiForType(@PathParam("poi-type") String poiType,List<PoiRecord> records) {
        var count = poiService.insertCustomPoiRecordsForType(poiType, records);
        return Response.created(UriBuilder.fromPath("poi/find/"+poiType+"/all").build())
                .entity("inserted "+count+" custom poi record(s) for type '"+poiType+"'")
                .build();
    }

    @GET
    @Path("find/all")
    @APIResponse(
            responseCode = "200",
            description = "get all points-of-interest irrespective of the specific poi type",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = PoiRecord.class)
            )
    )
    public Response findAllPoi() {
        var pois = poiService.findAllPois();
        Log.debugv("returning {0} poi documents of any type",pois.size());
        return Response.ok(pois).build();
    }

    @GET
    @Path("find/{poi-type}/all")
    @APIResponse(
            responseCode = "200",
            description = "get all points-of-interest for a specific poi type",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = PoiRecord.class)
            )
    )
    public Response findAllPoiByType(@PathParam("poi-type") String poiType) {
        var pois = poiService.findAllPoisByType(poiType);
        Log.debugv("returning {0} poi documents of type {1}",pois.size(),poiType);
        return Response.ok(pois).build();
    }

}
