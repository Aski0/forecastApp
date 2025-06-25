package com.example.forecastapp.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class DailyForecastDto {
    // data prognozy
    private LocalDate date;
    // kod pogody (odpowiadający typowi pogody np. słońce, deszcz, chmury)
    private int weatherCode;
    // minimalna temperatura w danym dniu
    private double minTemperature;
    // maksymalna temperatura w danym dniu
    private double maxTemperature;
    // szacowana wyprodukowana energia (kWh) na podstawie nasłonecznienia i mocy PV (fotowoltaiki)
    private double generatedEnergyKwh;
}
