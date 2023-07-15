package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Flight;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Data
@Repository
public class FlightRepository {
    private List<Flight> addedFlights;
    public FlightRepository(List<Flight> addedFlights) {
        this.addedFlights = addedFlights;
    }

    public Flight fetchFlight(String id) {
        return addedFlights.stream()
                .filter(flight -> flight.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with this ID doesn't exist!"));
    }

    public void setAddedFlights(List<Flight> updatedFlights) {
        this.addedFlights = updatedFlights;
    }
}
