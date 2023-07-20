package io.codelex.flightplanner.flighttests;

import io.codelex.flightplanner.controller.AdminClientController;
import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.requests.AddFlightRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CanAddFlightTest {
    @Autowired
    AdminClientController adminController;

    @Test
    public void canAddFlight() {
        //Given
        Airport fromAirport = new Airport("Latvia", "Riga", "RIX");
        Airport toAirport = new Airport("Sweden", "Stockholm", "ARN");
        String departureTime = "2023-07-14 10:00";
        String arrivalTime = "2023-07-14 12:00";
        String carrier = "AirBaltic";
        AddFlightRequest request = new AddFlightRequest(fromAirport, toAirport, carrier, departureTime,
                arrivalTime);

        //When
        Flight savedFlight = adminController.addFlight(request);

        //Then
        assertNotNull(savedFlight.getId());
        Assertions.assertEquals("RIX", savedFlight.getFrom().getAirport());
        Assertions.assertEquals("ARN", savedFlight.getTo().getAirport());
        Assertions.assertEquals("Sweden", savedFlight.getTo().getCountry());
        Assertions.assertEquals("AirBaltic", savedFlight.getCarrier());
        Assertions.assertEquals(departureTime, savedFlight.getDepartureTime());
        Assertions.assertEquals(arrivalTime, savedFlight.getArrivalTime());

    }
}