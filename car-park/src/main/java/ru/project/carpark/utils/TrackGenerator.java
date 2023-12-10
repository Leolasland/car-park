package ru.project.carpark.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.project.carpark.service.CarTrackService;

@ShellComponent
@RequiredArgsConstructor
public class TrackGenerator {

    private final CarTrackService carTrackService;

    @ShellMethod(key = "generate-track", value = "Generate track for vehicle with introduced id")
    private String generateTrack(@ShellOption({"id", "-i"}) String id,
                                 @ShellOption({"country", "-C"}) String country,
                                 @ShellOption({"city", "-c"}) String city,
                                 @ShellOption(defaultValue = ShellOption.NULL, value = {"range", "-r"},
                                 help = "Range introduced in km") String range,
                                 @ShellOption(defaultValue = ShellOption.NULL, value = {"point", "-p"},
                                 help = "Point step") String point) {
        return carTrackService.generateTrack(Integer.parseInt(id), country, city,
                range != null ? Integer.parseInt(range) : 0,
                point != null ? Double.parseDouble(point) : 0);
    }
}
