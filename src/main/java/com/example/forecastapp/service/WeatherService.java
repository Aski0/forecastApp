package com.example.forecastapp.service;

import com.example.forecastapp.dto.DailyForecastDto;
import com.example.forecastapp.dto.OpenMeteoResponseDto;
import com.example.forecastapp.dto.WeeklySummaryDto;
import com.example.forecastapp.model.DailyData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient webClient;
    private static final double PV_POWER_KW = 2.5;
    private static final double PV_EFFICIENCY = 0.2;

    public List<DailyForecastDto> get7DayForecast(double latitude, double longitude) {
        String[] dailyParams = {"weather_code", "temperature_2m_max", "temperature_2m_min", "daylight_duration"};
        OpenMeteoResponseDto meteoResponse = fetchWeatherData(latitude, longitude, dailyParams, null); // Brak danych godzinowych
        return processMeteoResponseFor7DayForecast(meteoResponse);
    }

    public WeeklySummaryDto getWeeklySummary(double latitude, double longitude) {
        // ZMIANA: Prosimy o te same dane dzienne co poprzednio
        String[] dailyParams = {"weather_code", "temperature_2m_max", "temperature_2m_min", "daylight_duration"};
        // ZMIANA: Osobno prosimy o dane godzinowe dla ciśnienia
        String[] hourlyParams = {"pressure_msl"};

        OpenMeteoResponseDto meteoResponse = fetchWeatherData(latitude, longitude, dailyParams, hourlyParams);
        return processMeteoResponseForSummary(meteoResponse);
    }

    /**
     * Zaktualizowana metoda, która przyjmuje zarówno parametry dzienne, jak i godzinowe.
     */
    private OpenMeteoResponseDto fetchWeatherData(double latitude, double longitude, String[] dailyParams, String[] hourlyParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("forecast_days", 7);

        if (dailyParams != null && dailyParams.length > 0) {
            builder.queryParam("daily", String.join(",", dailyParams));
        }
        if (hourlyParams != null && hourlyParams.length > 0) {
            builder.queryParam("hourly", String.join(",", hourlyParams));
        }

        URI url = builder.build().toUri();
        log.info("Fetching weather data from URL: {}", url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(OpenMeteoResponseDto.class)
                .doOnError(e -> log.error("Error fetching weather data", e))
                .block();
    }

    /**
     * Całkowicie nowa logika obliczania średniego ciśnienia z danych godzinowych.
     */
    private WeeklySummaryDto processMeteoResponseForSummary(OpenMeteoResponseDto response) {
        if (response == null || response.getDaily() == null) {
            log.error("Brak danych pogodowych w odpowiedzi od API.");
            throw new IllegalStateException("Nie udało się pobrać danych pogodowych z zewnętrznego serwisu.");
        }
        DailyData dailyData = response.getDaily();

        // --- NOWA LOGIKA OBLICZANIA CIŚNIENIA ---
        double weeklyAveragePressure = 0.0;
        if (response.getHourly() != null && response.getHourly().getPressureMsl() != null) {
            List<Double> hourlyPressures = response.getHourly().getPressureMsl();
            // Obliczamy średnią z całej listy godzinowej (7 dni * 24 godziny)
            weeklyAveragePressure = hourlyPressures.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
        }

        // --- Reszta logiki pozostaje bez zmian, używa `dailyData` ---
        double averageDaylight = dailyData.getDaylightDuration().stream()
                .mapToDouble(d -> d / 3600.0)
                .average()
                .orElse(0.0);

        double minTemp = Collections.min(dailyData.getTemperatureMin());
        double maxTemp = Collections.max(dailyData.getTemperatureMax());

        long rainyDays = dailyData.getWeatherCode().stream()
                .filter(this::isPrecipitation)
                .count();
        String weatherSummary = (rainyDays >= 4) ? "Tydzień z opadami" : "Tydzień w większości bez opadów";

        return WeeklySummaryDto.builder()
                .averagePressureHpa(Math.round(weeklyAveragePressure * 10.0) / 10.0) // Używamy naszej nowej zmiennej
                .averageDaylightDurationHours(Math.round(averageDaylight * 10.0) / 10.0)
                .minTemperatureWeek(minTemp)
                .maxTemperatureWeek(maxTemp)
                .weatherSummary(weatherSummary)
                .build();
    }

    // ... reszta klasy (isPrecipitation, processMeteoResponseFor7DayForecast) bez zmian ...
    private boolean isPrecipitation(int code) {
        return code >= 51;
    }

    private List<DailyForecastDto> processMeteoResponseFor7DayForecast(OpenMeteoResponseDto response) {
        if (response == null || response.getDaily() == null) {
            return List.of();
        }
        List<DailyForecastDto> forecasts = new ArrayList<>();
        var dailyData = response.getDaily();
        int days = dailyData.getTime().size();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        for (int i = 0; i < days; i++) {
            double daylightDurationInSeconds = dailyData.getDaylightDuration().get(i);
            double daylightDurationInHours = daylightDurationInSeconds / 3600.0;
            double generatedEnergy = PV_POWER_KW * daylightDurationInHours * PV_EFFICIENCY;
            DailyForecastDto dto = DailyForecastDto.builder()
                    .date(LocalDate.parse(dailyData.getTime().get(i), formatter))
                    .weatherCode(dailyData.getWeatherCode().get(i))
                    .minTemperature(dailyData.getTemperatureMin().get(i))
                    .maxTemperature(dailyData.getTemperatureMax().get(i))
                    .generatedEnergyKwh(Math.round(generatedEnergy * 100.0) / 100.0)
                    .build();
            forecasts.add(dto);
        }
        return forecasts;
    }
}