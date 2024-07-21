package ru.project.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import org.geojson.GeoJsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.project.carpark.service.OpenRouteIntegrationService;

@Service
@RequiredArgsConstructor
public class OpenRouteIntegrationServiceImpl implements OpenRouteIntegrationService {

    private final RestTemplate restTemplate;

    @Value("${openroute.header}")
    private String headerValue;

    @Value("${openroute.geocode.reverse.url}")
    private String geocodeReverseUrl;

    public GeoJsonObject getGeocodeReverse(double lon, double lan) {
        String url = geocodeReverseUrl + headerValue + "&point.lon=" + lon + "&point.lat=" + lan +
                "&sources=openaddresses";
        ResponseEntity<GeoJsonObject> response = restTemplate.getForEntity(url, GeoJsonObject.class);
        return response.getBody();
    }
}
