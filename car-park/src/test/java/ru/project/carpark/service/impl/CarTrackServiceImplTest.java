package ru.project.carpark.service.impl;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.project.carpark.BaseTest;
import ru.project.carpark.entity.CarTrack;
import ru.project.carpark.service.CarTrackService;
import ru.project.carpark.service.RideService;

import static org.junit.jupiter.api.Assertions.*;

class CarTrackServiceImplTest extends BaseTest {

    @Autowired
    private CarTrackService carTrackService;
    @MockBean
    private RideService rideService;

    @Test
    void findById() {
        CarTrack result = carTrackService.findById(1);
        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());
    }

    @Test
    void findByIdNone() {
        CarTrack result = carTrackService.findById(1105000);
        assertNull(result);
    }

    @Test
    void saveCoordinatesNone() {
        String result = carTrackService.saveCoordinates(1105000, new FeatureCollection());
        assertEquals("Vehicle not found", result);

        result = carTrackService.saveCoordinates(1, new FeatureCollection());
        assertEquals("Wrong params for city or country", result);
    }

    @Disabled
    @Test
    void saveCoordinates() {
        LngLatAlt location = new LngLatAlt(1, 1);
        LineString lineString = new LineString();
        lineString.add(location);
        Feature feature = new Feature();
        feature.setGeometry(lineString);
        FeatureCollection track = new FeatureCollection();
        track.add(feature);
        String result = carTrackService.saveCoordinates(3, track);
        assertEquals("Success generation", result);
    }

    @Test
    void mapToGeo() {
    }
}