package io.codelex.flightplanner.flights.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Flight {
    private String id;
    private Airport from;
    private Airport to;
    private String carrier;
    @JsonProperty("departureTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String departureTime;
    @JsonProperty("arrivalTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String arrivalTime;

    public Flight(String id, Airport from, Airport to, String carrier, String departureTime, String arrivalTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Flight(Airport from, Airport to, String carrier, String departureTime, String arrivalTime) {
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Flight() {
    }

}
