package com.rhdevelopers.summit.workshop;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PoiRecord(
    String name,
    String description,
    List<Double> coordinates,
    Map<String,String> paramsString,
    Map<String,Double> paramsNumeric
) {}
