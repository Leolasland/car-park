package ru.project.carpark.service;

import org.geojson.GeoJsonObject;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.entity.CarTrack;

import java.util.List;

public interface CarTrackService {

    CarTrack findById(Integer id);

    String saveCoordinates(Integer id, GeoJsonObject track);

    GeoJsonObject mapToGeo(List<CarTrackDto> trackDtoList, Integer id);
}
