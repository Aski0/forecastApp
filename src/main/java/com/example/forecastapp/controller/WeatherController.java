package com.example.forecastapp.controller;

import com.example.forecastapp.dto.DailyForecastDto;
import com.example.forecastapp.dto.WeeklySummaryDto;
import com.example.forecastapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;
    @GetMapping("/forecast")
    public List<DailyForecastDto> get7DayForecast(
            @RequestParam double latitude,
            @RequestParam double longitude){
        return weatherService.get7DayForecast(latitude, longitude);
    }
    @GetMapping("/summary")
    public WeeklySummaryDto getWeeklySummary(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        return weatherService.getWeeklySummary(latitude, longitude);
    }
}
