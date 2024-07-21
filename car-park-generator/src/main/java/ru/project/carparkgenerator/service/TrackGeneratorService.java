package ru.project.carparkgenerator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class TrackGeneratorService {

    private final CarTrackGeneratorService carTrackGeneratorService;

    @ShellMethod(key = "generate-track", value = "Generate track for vehicle with introduced id")
    private String generateTrack(@ShellOption({"id", "-i"}) String id,
                                 @ShellOption({"country", "-C"}) String country,
                                 @ShellOption({"city", "-c"}) String city,
                                 @ShellOption(defaultValue = ShellOption.NULL, value = {"range", "-r"},
                                 help = "Range introduced in km") String range,
                                 @ShellOption(defaultValue = ShellOption.NULL, value = {"point", "-p"},
                                 help = "Point step") String point) {
        return carTrackGeneratorService.generateTrack(Integer.parseInt(id), country, city,
                range != null ? Integer.parseInt(range) : 0,
                point != null ? Double.parseDouble(point) : 0);
    }
}
