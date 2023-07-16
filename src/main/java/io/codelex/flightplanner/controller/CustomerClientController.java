package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.requests.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.PageResult;
import io.codelex.flightplanner.flights.service.FlightService;
import io.codelex.flightplanner.flights.domain.Airport;
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
    @PostMapping("/flights/search")
    public PageResult<Flight> searchFlights(SearchFlightRequest flightRequest) {
       return this.flightService.searchFlights(flightRequest);
    }
}
