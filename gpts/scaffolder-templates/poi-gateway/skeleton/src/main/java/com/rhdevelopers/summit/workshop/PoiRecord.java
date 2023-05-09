package com.rhdevelopers.summit.workshop;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PoiRecord(
    String name,
    String description,
    List<Double> coordinates
) {}
