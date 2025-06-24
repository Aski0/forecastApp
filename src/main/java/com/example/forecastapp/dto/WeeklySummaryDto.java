package com.example.forecastapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeeklySummaryDto {
    private double averagePressureHpa;
    private double averageDaylightDurationHours;
    private double minTemperatureWeek;
    private double maxTemperatureWeek;
    private String weatherSummary;
}
