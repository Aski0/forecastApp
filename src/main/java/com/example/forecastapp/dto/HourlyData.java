package com.example.forecastapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class HourlyData {

    @JsonProperty("time")
    private List<String> time; // Lista dat i godzin

    @JsonProperty("pressure_msl")
    private List<Double> pressureMsl;
}
