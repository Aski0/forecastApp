package com.example.forecastapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//obiekt do przekazyywania podsumowania pogody
public class WeeklySummaryDto {
    // średnie ciśnienie atmosferyczne w hPa w ciągu tygodnia
    private double averagePressureHpa;
    // średnia długość dnia (czas nasłonecznienia) w godzinach
    private double averageDaylightDurationHours;
    // najniższa temperatura w tygodniu
    private double minTemperatureWeek;
    // najwyższa temperatura w tygodniu
    private double maxTemperatureWeek;
    // ogólny opis tygodnia, np. "Tydzień z opadami"
    private String weatherSummary;
}
