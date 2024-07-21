package ru.project.carparkgenerator.service.impl;

import lombok.RequiredArgsConstructor;
import org.geojson.GeoJsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.project.carparkgenerator.dto.openroute.RequestOpenRouteDto;
import ru.project.carparkgenerator.service.OpenRouteIntegrationService;

@Service
@RequiredArgsConstructor
public class OpenRouteIntegrationServiceImpl implements OpenRouteIntegrationService {

    private final RestTemplate restTemplate;

    @Value("${openroute.header}")
    private String headerValue;

    @Value("${openroute.geocode.url}")
    private String geocodeUrl;

    @Value("${openroute.area.url}")
    private String areaUrl;

    @Value("${openroute.track.url}")
    private String trackUrl;

    public GeoJsonObject getGeocode(String country, String city) {
        String url = geocodeUrl + headerValue +
                "&country=" + country +
                "&locality=" + city;
        ResponseEntity<GeoJsonObject> response = restTemplate.getForEntity(url, GeoJsonObject.class);
        return response.getBody();
    }

    public GeoJsonObject getArea(RequestOpenRouteDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", headerValue);
        HttpEntity<Object> request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<GeoJsonObject> response = restTemplate.postForEntity(areaUrl, request, GeoJsonObject.class);
        return response.getBody();
    }

    public GeoJsonObject getTrack(String start, String end) {
        String url = trackUrl + headerValue +
                "&start=" + start +
                "&end=" + end;
        ResponseEntity<GeoJsonObject> response = restTemplate.getForEntity(url, GeoJsonObject.class);
        return response.getBody();
    }
}
