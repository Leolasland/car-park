package ru.project.carparkgenerator.dto;

import org.geojson.GeoJsonObject;

public record SaveCoordinatesMessageDto(
        Integer id,
        GeoJsonObject track
) {
}
