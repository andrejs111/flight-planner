package io.codelex.flightplanner.flighttests;

import io.codelex.flightplanner.controller.AdminClientController;
import io.codelex.flightplanner.controller.TestingClientController;
import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.requests.AddFlightRequest;
import io.codelex.flightplanner.flights.service.FlightService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
public class CanClearFlightsTest {
    @Autowired
    TestingClientController testController;
    @Autowired
    AdminClientController adminController;

    @Autowired
    FlightService flightService;

    @Test
    public void canClearFlights() {
        // Given
        Airport fromAirport = new Airport("Latvia", "Riga", "RIX");
        Airport toAirport = new Airport("Sweden", "Stockholm", "ARN");
        LocalDateTime departureTime = LocalDateTime.of(2023, 7, 14, 10, 0);
        LocalDateTime arrivalTime = LocalDateTime.of(2023, 7, 14, 12, 0);
        String carrier = "AirBaltic";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        AddFlightRequest request = new AddFlightRequest(fromAirport, toAirport, carrier, format.format(departureTime),
                format.format(arrivalTime));
        adminController.addFlight(request);

        //When
        flightService.getFlights();

        //Then
        Assertions.assertEquals(flightService.getFlights().size(), 1);

        //When
        testController.clearFlights();

        //Then
        Assertions.assertEquals(flightService.getFlights().size(), 0);
    }
}
