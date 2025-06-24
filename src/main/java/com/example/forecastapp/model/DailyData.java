package com.example.forecastapp.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
@Data
public class DailyData {

    @JsonProperty("time")
    private List<String> time;

    @JsonProperty("weather_code")
    private List<Integer> weatherCode;

    @JsonProperty("temperature_2m_max")
    private List<Double> temperatureMax;

    @JsonProperty("temperature_2m_min")
    private List<Double> temperatureMin;

    @JsonProperty("daylight_duration")
    private List<Double> daylightDuration;

    @JsonProperty("pressure_msl")
    private List<Double> pressureMsl;
}
