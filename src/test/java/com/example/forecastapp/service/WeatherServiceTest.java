package com.example.forecastapp.service;

import com.example.forecastapp.dto.HourlyData;
import com.example.forecastapp.dto.OpenMeteoResponseDto;
import com.example.forecastapp.dto.WeeklySummaryDto;
import com.example.forecastapp.model.DailyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock // tworzy atrapę (mocka) WebClienta
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks // Tworzy instancję WeatherService i wstrzykuje do niej powyższe mocki
    private WeatherService weatherService;

    @Test
    void getWeeklySummary_shouldCalculateCorrectSummary_whenApiReturnsData() {
        // fałszywa odpowiedź, którą zwróci API
        OpenMeteoResponseDto fakeResponse = new OpenMeteoResponseDto();
        DailyData dailyData = new DailyData();
        dailyData.setTemperatureMin(List.of(10.0, 12.0));
        dailyData.setTemperatureMax(List.of(20.0, 22.0));
        dailyData.setDaylightDuration(List.of(36000.0, 72000.0)); // 10h, 20h
        dailyData.setWeatherCode(List.of(3, 3, 3, 61, 61, 61, 61)); // 4 deszczowe dni
        fakeResponse.setDaily(dailyData);

        HourlyData hourlyData = new HourlyData();
        hourlyData.setPressureMsl(List.of(1000.0, 1020.0)); // średnia 1010
        fakeResponse.setHourly(hourlyData);

        // pożądane zachowanie WebClient
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.net.URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenMeteoResponseDto.class)).thenReturn(Mono.just(fakeResponse));

        // wywołanie testowanej metody
        WeeklySummaryDto result = weatherService.getWeeklySummary(52.0, 21.0);

        // sprawdzenie wyniku z oczekiwaniami
        assertThat(result).isNotNull();
        assertThat(result.getMinTemperatureWeek()).isEqualTo(10.0);
        assertThat(result.getMaxTemperatureWeek()).isEqualTo(22.0);
        assertThat(result.getWeatherSummary()).isEqualTo("Tydzień z opadami");
        assertThat(result.getAveragePressureHpa()).isEqualTo(1010.0);
        assertThat(result.getAverageDaylightDurationHours()).isEqualTo(15.0); // (10+20)/2
    }
}