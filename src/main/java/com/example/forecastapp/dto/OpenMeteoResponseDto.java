package com.example.forecastapp.dto;
import com.example.forecastapp.model.DailyData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class OpenMeteoResponseDto {
    @JsonProperty("daily")
    private DailyData daily;

    @JsonProperty("hourly")
    private HourlyData hourly;
}
