package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Flight;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlightRepository {
    private final List<Flight> addedFlights;
    private List<Integer> generatedIds;

    public FlightRepository(List<Flight> addedFlights) {
        this.addedFlights = addedFlights;
    }

    public List<Flight> listFlights() {
        return this.addedFlights;
    }
    public Flight fetchFlight(String id) {
        return addedFlights.stream()
                .filter(flight -> flight.getId().equals(id))
                .findAny()
                .orElse(null);
    }
}
