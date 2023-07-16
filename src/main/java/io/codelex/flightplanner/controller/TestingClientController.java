package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.flights.repositories.FlightRepository;
import io.codelex.flightplanner.flights.service.FlightService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing-api")
public class TestingClientController {

    FlightService flightService;
    FlightRepository flightRepository;

    public TestingClientController(FlightService flightService, FlightRepository flightRepository) {
        this.flightService = flightService;
        this.flightRepository = flightRepository;
    }
    @PostMapping("/clear")
    public void clearFlights() {
        this.flightService.clearFlights();
    }


}
