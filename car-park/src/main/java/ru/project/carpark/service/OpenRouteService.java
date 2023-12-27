package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import org.geojson.GeoJsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.project.carpark.dto.openroute.RequestOpenRouteDto;

@Service
@RequiredArgsConstructor
public class OpenRouteService {

    private final RestTemplate restTemplate;

    @Value("${openroute.header}")
    private String headerValue;

    @Value("${openroute.geocode.url}")
    private String geocodeUrl;

    @Value("${openroute.area.url}")
    private String areaUrl;

    @Value("${openroute.track.url}")
    private String trackUrl;

    @Value("${openroute.geocode.reverse.url}")
    private String geocodeReverseUrl;

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

    public GeoJsonObject getGeocodeReverse(double lon, double lan) {
        String url = geocodeReverseUrl + headerValue + "&point.lon=" + lon + "&point.lat=" + lan +
                "&sources=openaddresses";
        ResponseEntity<GeoJsonObject> response = restTemplate.getForEntity(url, GeoJsonObject.class);
        return response.getBody();
    }
}
