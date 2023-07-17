package io.codelex.flightplanner.flighttests;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.repositories.FlightRepository;
import io.codelex.flightplanner.flights.requests.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.PageResult;
import io.codelex.flightplanner.flights.service.FlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SearchFlightTest {
    @Mock
    FlightRepository flightsRepository;
    @InjectMocks
    FlightService flightsService;

    @Test
    public void testSearchFlight() {
        // Given
        SearchFlightRequest request = new SearchFlightRequest("RIX", "ARN", "2023-07-14");
        Airport fromAirport = new Airport("Latvia", "Riga", "RIX");
        Airport toAirport = new Airport("Sweden", "Stockholm", "ARN");
        String departureTime = "2023-07-14 10:00";
        String arrivalTime = "2023-07-14 12:00";
        Flight flight = new Flight("1", fromAirport, toAirport, "AirBaltic", departureTime, arrivalTime);
        List<Flight> mockFlights = Collections.singletonList(flight);
        Mockito.when(flightsRepository.getAddedFlights()).thenReturn(mockFlights);

        // When
        PageResult<Flight> result = flightsService.searchFlights(request);

        // Then
        Mockito.verify(flightsRepository).getAddedFlights();
        assertEquals(1, result.getItems().size());
        assertEquals(flight, result.getItems().get(0));
    }
}
