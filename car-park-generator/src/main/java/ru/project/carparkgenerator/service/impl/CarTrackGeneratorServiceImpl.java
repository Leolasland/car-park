package ru.project.carparkgenerator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geojson.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.project.carparkgenerator.dto.SaveCoordinatesMessageDto;
import ru.project.carparkgenerator.dto.openroute.RequestOpenRouteDto;
import ru.project.carparkgenerator.producer.CarTrackSendCoordinatorProducer;
import ru.project.carparkgenerator.service.CarTrackGeneratorService;
import ru.project.carparkgenerator.utils.Generator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarTrackGeneratorServiceImpl implements CarTrackGeneratorService {

    private final OpenRouteIntegrationServiceImpl openRouteService;
    private final CarTrackSendCoordinatorProducer carTrackSendCoordinatorProducer;

    @Override
    public String generateTrack(Integer id, String country, String city, int range, double point) {
        GeoJsonObject geocode = openRouteService.getGeocode(country, city);
        List<Feature> features = ((FeatureCollection) geocode).getFeatures();
        if (features == null || features.isEmpty()) {
            return "Wrong params for city or country";
        }
        RequestOpenRouteDto requestDto = new RequestOpenRouteDto();
        requestDto.setLocations(convertCoordinates(getCoordinates(features)));
        if (point != 0) {
            requestDto.setInterval(point);
        }
        if (range != 0) {
            int rangeInKm = range * 1000;
            requestDto.setRange(List.of(rangeInKm, rangeInKm - 100));
        }
        GeoJsonObject area = openRouteService.getArea(requestDto);
        List<Feature> areaFeatures = ((FeatureCollection) area).getFeatures();
        if (areaFeatures == null || areaFeatures.isEmpty()) {
            return "Wrong params for city or country";
        }
        GeoJsonObject track = openRouteService.getTrack(getRandomRouteCoordinates(areaFeatures).getFirst(),
                getRandomRouteCoordinates(areaFeatures).getSecond());
        SaveCoordinatesMessageDto message = new SaveCoordinatesMessageDto(id, track);
        try {
            carTrackSendCoordinatorProducer.sendMessage(message);
            return "Track generated successfully";
        } catch (Exception e) {
            return "Track generation failed" + e.getMessage();
        }
    }

    private List<List<Double>> convertCoordinates(double[] array) {
        List<List<Double>> listOfLists = new ArrayList<>();

        for (int i = 0; i < array.length; i += 2) {
            List<Double> innerList = new ArrayList<>();
            innerList.add(array[i]);
            innerList.add(array[i + 1]);
            listOfLists.add(innerList);
        }
        return listOfLists;
    }

    private double[] getCoordinates(List<Feature> features) {
        Point point = (Point) features.get(0).getGeometry();
        return new double[]{point.getCoordinates().getLongitude(), point.getCoordinates().getLatitude()};
    }

    private Pair<String, String> getRandomRouteCoordinates(List<Feature> features) {
        List<LngLatAlt> randomRange;
        LngLatAlt start;
        LngLatAlt end;
        if (Generator.generateInt() % 2 == 0) {
            Polygon geometry = (Polygon) features.get(0).getGeometry();
            randomRange = geometry.getCoordinates().get(0);
        } else {
            Polygon geometry = (Polygon) features.get(1).getGeometry();
            randomRange = geometry.getCoordinates().get(0);
        }
        if (Generator.generateInt() % 2 == 0) {
            start = randomRange.get(0);
            end = randomRange.get(randomRange.size() / 2);
        } else {
            start = randomRange.get(randomRange.size() / 2);
            end = randomRange.get(0);
        }
        return Pair.of(getCoordinates(start), getCoordinates(end));
    }

    private String getCoordinates(LngLatAlt point) {
        return String.valueOf(point.getLongitude()) + ',' + point.getLatitude();
    }
}
