package ru.project.carpark.service;

import org.geojson.GeoJsonObject;

public interface OpenRouteIntegrationService {

    GeoJsonObject getGeocodeReverse(double lon, double lan);
}
