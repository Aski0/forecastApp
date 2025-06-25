package com.example.forecastapp.controller;

import com.example.forecastapp.dto.WeeklySummaryDto;
import com.example.forecastapp.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc; // obiekt do wykonywania fałszywych żądań HTTP

    @MockBean // zastępuje prawdziwy WeatherService
    private WeatherService weatherService;

    @Test
    void getWeeklySummary_shouldReturnSummaryDto_whenParamsAreValid() throws Exception {
        // fałszywy obiekt DTO, który ma zwrócić serwis
        WeeklySummaryDto fakeSummary = WeeklySummaryDto.builder()
                .maxTemperatureWeek(25.5)
                .minTemperatureWeek(10.1)
                .weatherSummary("Tydzień w większości bez opadów")
                .build();

        when(weatherService.getWeeklySummary(anyDouble(), anyDouble())).thenReturn(fakeSummary);

        // wykonaj żądanie GET i sprawdź odpowiedź
        mockMvc.perform(get("/api/v1/weather/summary")
                        .param("latitude", "52.0")
                        .param("longitude", "21.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.maxTemperatureWeek").value(25.5))
                .andExpect(jsonPath("$.weatherSummary").value("Tydzień w większości bez opadów"));
    }

    @Test
    void getWeeklySummary_shouldReturnBadRequest_whenLatitudeIsInvalid() throws Exception {
        // sprwdzanie walidacji
        mockMvc.perform(get("/api/v1/weather/summary")
                        .param("latitude", "100.0") // nieprawidłowa wartość
                        .param("longitude", "21.0"))
                .andExpect(status().isBadRequest());
    }
}