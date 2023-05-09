package com.rhdevelopers.summit.workshop.poi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;

@Startup
public class PoiService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @ConfigProperty(name = "poi.parks.file")
    String poiParksFile;

    @Inject
    PoiRepository poiRepository;

    @ConfigProperty(name = "poi.parks.auto.load")
    boolean poiParksAutoLoad;

    boolean parksDataLoaded = false;

    @PostConstruct
    public void autoLoadParksData() {
        if(poiParksAutoLoad) {
            Log.debug("auto-loading parks data");
            insertPresetPoiRecordsOfTypePark();
        }
    }

    @Transactional
    public int insertPresetPoiRecordsOfTypePark() {
        if(parksDataLoaded) {
            Log.debugv("parks data has already been initialized either automatically or manually");
            return 0;
        }
        Log.debugv("parsing parks data from file {0}", poiParksFile);
        try (InputStream inputStream = getClass().getResourceAsStream(poiParksFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                var pois = reader.lines()
                    .map(l -> {
                        try {
                            return PoiEntity.fromPoiRecord(
                                    OBJECT_MAPPER.readValue(l, PoiRecord.class),
                                    "park");
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("failed to parse parks data", e);
                        }
                    })
                    .collect(Collectors.toList());
            Log.debugv("persist {0} poi record(s)", pois.size());
            poiRepository.persist(pois);
            parksDataLoaded = true;
            return pois.size();
        } catch (IOException e) {
            throw new RuntimeException("failed to initialize parks data", e);
        }
    }

    @Transactional
    public int insertCustomPoiRecordsForType(String poiType, List<PoiRecord> records) {
        Log.debugv("mapping {0} record(s)", records.size());
        var pois = records.stream()
                .map(pr -> PoiEntity.fromPoiRecord(pr, poiType))
                .collect(Collectors.toList());
        Log.debugv("persist {0} custom poi record(s) for type {1}", pois.size(), poiType);
        poiRepository.persist(pois);
        return pois.size();
    }

    public List<PoiRecord> findAllPois() {
        Log.debugv("find all pois");
        return poiRepository.listAll()
                .stream().map(PoiRecord::fromPoiEntity)
                .collect(Collectors.toList());
    }

    public List<PoiRecord> findAllPoisByType(String poiType) {
        Log.debugv("find all pois for type {0}", poiType);
        return poiRepository.listByType(poiType)
                .stream().map(PoiRecord::fromPoiEntity)
                .collect(Collectors.toList());
    }

}
