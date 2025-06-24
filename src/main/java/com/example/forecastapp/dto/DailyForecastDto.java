package com.example.forecastapp.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class DailyForecastDto {
    private LocalDate date;
    private int weatherCode;
    private double minTemperature;
    private double maxTemperature;
    private double generatedEnergyKwh;
}
