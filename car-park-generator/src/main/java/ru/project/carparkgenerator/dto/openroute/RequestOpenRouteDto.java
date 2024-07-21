package ru.project.carparkgenerator.dto.openroute;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestOpenRouteDto {

    private List<List<Double>> locations;

    private List<Integer> range = List.of(3000, 2000);

    private Double interval;

    @JsonProperty("range_type")
    private String rangeType = "distance";
}
