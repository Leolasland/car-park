package ru.project.carpark.dto;

import org.geojson.GeoJsonObject;

public record SaveCoordinatesMessageDto(
        Integer id,
        GeoJsonObject track
) {
}
