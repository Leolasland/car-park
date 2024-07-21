package ru.project.carparkgenerator.service;

import org.geojson.GeoJsonObject;
import ru.project.carparkgenerator.dto.openroute.RequestOpenRouteDto;

public interface OpenRouteIntegrationService {

    GeoJsonObject getGeocode(String country, String city);

    GeoJsonObject getArea(RequestOpenRouteDto requestDto);

    GeoJsonObject getTrack(String start, String end);
}
