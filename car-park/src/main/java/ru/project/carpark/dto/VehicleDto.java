package ru.project.carpark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Сущность машины")
@Data
public class VehicleDto {

    @Schema(description = "Идентификатор", example = "21345")
    private Integer id;

    @Schema(description = "Цена", example = "2000000")
    private Long price;

    @Schema(description = "Год производства", example = "2010")
    private String yearManufacture;

    @Schema(description = "Марка машины", example = "BMW")
    private String carBrand;

    @Schema(description = "Дата покупки", example = "2024-01-13 17:10:28.071503 +00:00")
    private String dtBuy;
}
