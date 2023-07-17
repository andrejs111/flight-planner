package io.codelex.flightplanner.controller;


import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.requests.AddFlightRequest;
import io.codelex.flightplanner.flights.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-api")
public class AdminClientController {
    FlightService flightService;

    public AdminClientController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)                                      // because PUT returns 200 by default
    public Flight addFlight(@Valid @RequestBody AddFlightRequest flightRequest) {
        return this.flightService.addFlight(flightRequest);
    }

    @GetMapping("/flights/{id}")
    public Flight fetchFlight(@PathVariable String id) {
        return this.flightService.fetchFlight(id);
    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlight(@PathVariable String id) {
        this.flightService.deleteFlight(id);
    }
}
