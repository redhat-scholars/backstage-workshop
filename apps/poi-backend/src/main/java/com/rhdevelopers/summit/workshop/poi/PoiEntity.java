package com.rhdevelopers.summit.workshop.poi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "pois")
public class PoiEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;
    public String description;
    public double latitude;
    public double longitude;
    public String type;

    public PoiEntity() {}

    public PoiEntity(String name, String description, double latitude, double longitude, String type) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public static PoiEntity fromPoiRecord(PoiRecord poiRecord, String type) {
        return new PoiEntity(
            poiRecord.name(),poiRecord.description(),
            poiRecord.coordinates().get(0),poiRecord.coordinates().get(1),type
        );
    }

} 
