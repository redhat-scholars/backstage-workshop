package com.rhdevelopers.summit.workshop.poi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PoiRecord(
    String name,
    String description,
    List<Double> coordinates
) {
    public static PoiRecord fromPoiEntity(PoiEntity poiEntity) {
        return new PoiRecord(poiEntity.name,poiEntity.description,List.of(poiEntity.latitude,poiEntity.longitude));
    }
}
