package io.codelex.flightplanner.flights.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class Airport {

    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String airport;

    public Airport(String country, String city, String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public Airport() {
    }
}
