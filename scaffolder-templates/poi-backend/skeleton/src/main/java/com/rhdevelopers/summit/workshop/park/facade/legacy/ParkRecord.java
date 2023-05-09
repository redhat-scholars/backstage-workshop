package com.rhdevelopers.summit.workshop.park.facade.legacy;

import com.rhdevelopers.summit.workshop.poi.PoiRecord;

public record ParkRecord(    
    String name,
    String latitude,
    String longitude
)
{
    public static ParkRecord fromPoiRecord(PoiRecord record) {
        return new ParkRecord(
            record.name(),
            String.valueOf(record.coordinates().get(0)),
            String.valueOf(record.coordinates().get(1))
        );
    }
}
