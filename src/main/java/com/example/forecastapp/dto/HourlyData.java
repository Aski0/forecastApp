package com.example.forecastapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class HourlyData {

    // Lista dat i godzin
    @JsonProperty("time")
    private List<String> time;

    // ciśnienie atmosferyczne dla każdej godziny
    @JsonProperty("pressure_msl")
    private List<Double> pressureMsl;
}
