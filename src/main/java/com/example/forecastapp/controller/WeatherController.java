package com.example.forecastapp.controller;

import com.example.forecastapp.dto.DailyForecastDto;
import com.example.forecastapp.dto.WeeklySummaryDto;
import com.example.forecastapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
//@CrossOrigin(origins = "*")
public class WeatherController {
    private final WeatherService weatherService;
    @GetMapping("/forecast")
    public List<DailyForecastDto> get7DayForecast(
            //szerokość geograficzna + walidacja
            @RequestParam("lat")
            @Min(value = -90, message = "Szerokość geograficzna musi być większa lub równa -90")
            @Max(value = 90, message = "Szerokość geograficzna musi być mniejsza lub równa 90")
            double latitude,
            //długość geograficzna + walidacja
            @RequestParam("lon")
            @Min(value = -180, message = "Długość geograficzna musi być większa lub równa -180")
            @Max(value = 180, message = "Długość geograficzna musi być mniejsza lub równa 180")
            double longitude) {
        return weatherService.get7DayForecast(latitude, longitude);
    }

    @GetMapping("/summary")
    public WeeklySummaryDto getWeeklySummary(
            @RequestParam("lat")
            @Min(value = -90, message = "Szerokość geograficzna musi być większa lub równa -90")
            @Max(value = 90, message = "Szerokość geograficzna musi być mniejsza lub równa 90")
            double latitude,
            @RequestParam("lon")
            @Min(value = -180, message = "Długość geograficzna musi być większa lub równa -180")
            @Max(value = 180, message = "Długość geograficzna musi być mniejsza lub równa 180")
            double longitude) {
        return weatherService.getWeeklySummary(latitude, longitude);
    }
}
