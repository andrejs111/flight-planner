package io.codelex.flightplanner.flights.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class SearchFlightRequest {
    private String from;
    private String to;
    @JsonFormat(pattern = "YYYY-MM-DD")
    private String departureDate;

    public SearchFlightRequest(String from, String to, String departureDate) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
    }

    public SearchFlightRequest() {
    }
}
