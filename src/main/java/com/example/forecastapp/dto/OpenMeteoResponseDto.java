package com.example.forecastapp.dto;
import com.example.forecastapp.model.DailyData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class OpenMeteoResponseDto {
    // dane dzienne z odpowiedzi API (pogoda, temperatura, długość dnia)
    @JsonProperty("daily")
    private DailyData daily;
    // dane godzinowe (np. ciśnienie co godzinę) — inna struktura
    @JsonProperty("hourly")
    private HourlyData hourly;
}
