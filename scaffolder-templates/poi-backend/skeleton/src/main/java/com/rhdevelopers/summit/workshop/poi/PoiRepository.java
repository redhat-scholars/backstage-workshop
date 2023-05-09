package com.rhdevelopers.summit.workshop.poi;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PoiRepository implements PanacheRepository<PoiEntity> {

    public List<PoiEntity> listByType(String type) {
        return list("type",type);
    }

}
