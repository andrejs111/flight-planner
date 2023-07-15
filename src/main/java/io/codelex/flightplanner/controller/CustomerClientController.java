package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.flights.FlightRepository;
import io.codelex.flightplanner.flights.FlightService;
import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.requests.AddFlightRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CustomerClientController {
    FlightService flightService;

    public CustomerClientController(FlightService flightService) {
        this.flightService = flightService;
    }
    @GetMapping("/airports")
    public Airport[] searchAirports(@RequestParam String search) {
        return this.flightService.searchAirports(search);
    }
}
