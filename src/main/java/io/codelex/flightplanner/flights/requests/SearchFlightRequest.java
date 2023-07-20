package io.codelex.flightplanner.flights.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchFlightRequest {

    @NotBlank
    private String from;
    @NotBlank
    private String to;
    @NotBlank
    @JsonFormat(pattern = "YYYY-MM-DD")
    private String departureDate;

    public SearchFlightRequest(String from, String to, String departureDate) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
    }
}
