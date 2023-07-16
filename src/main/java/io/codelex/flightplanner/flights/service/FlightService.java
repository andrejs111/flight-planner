package io.codelex.flightplanner.flights.service;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.repositories.FlightIdRepository;
import io.codelex.flightplanner.flights.repositories.FlightRepository;
import io.codelex.flightplanner.flights.requests.AddFlightRequest;
import io.codelex.flightplanner.flights.requests.SearchFlightRequest;
import io.codelex.flightplanner.flights.response.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class FlightService {

    FlightRepository flightRepository;
    FlightIdRepository flightIdRepository;

    public FlightService(FlightRepository flightRepository, FlightIdRepository flightIdRepository) {
        this.flightRepository = flightRepository;
        this.flightIdRepository = flightIdRepository;
    }

    ///// Admin client methods

    public Flight fetchFlight(String id) {
        Flight flight = flightRepository.fetchFlight(id);
        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with such ID doesn't exist!");
        }
        return flight;
    }

    public synchronized Flight addFlight(AddFlightRequest flightRequest) {

        Airport departureAirport = new Airport(flightRequest.getFrom().getCountry(), flightRequest.getFrom().getCity(), flightRequest.getFrom().getAirport());
        Airport arrivalAirport = new Airport(flightRequest.getTo().getCountry(), flightRequest.getTo().getCity(), flightRequest.getTo().getAirport());

        String departureTime = flightRequest.getDepartureTime();
        String arrivalTime = flightRequest.getArrivalTime();

        String flightId = generateID(flightRequest);

        Flight flightToAdd = new Flight(flightId, departureAirport, arrivalAirport, flightRequest.getCarrier(),
                departureTime, arrivalTime);

        if (checkIfFlightExists(flightToAdd)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight ID already exists!");
        }
        if (checkAirports(departureAirport, arrivalAirport)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight can't land in the same airport!");
        }
        if (checkTimes(departureTime, arrivalTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure and arrival times don't make sense!");
        }
        this.flightRepository.getAddedFlights().add(flightToAdd);
        return flightToAdd;
    }

    public synchronized void deleteFlight(String id) {
        List<Flight> flights = this.flightRepository.getAddedFlights();
        flights.removeIf(flight -> flight.getId().equals(id));
    }


    public String generateID(AddFlightRequest flightRequest) {
        Random random = new Random();

        boolean idExists;
        int number;
        String id;

        StringBuilder newId = new StringBuilder();
        newId.append(flightRequest.getDepartureTime().replaceAll("\\D+", ""));
        newId.append(flightRequest.getFrom().getAirport().replaceAll("[^A-Z]", ""));

        do {
            number = random.nextInt(flightIdRepository.getID_UPPER_BOUND()) + flightIdRepository.getID_LOWER_BOUND();
            if (number < 0) {
                number *= -1;
            }
            newId.append(number);
            id = newId.toString();
            idExists = flightIdRepository.getGeneratedIds().contains(id);
        } while (idExists);

        return id;
    }

    ///// Customer client methods

    public Airport[] searchAirports(String string) {
        String pattern = string.toLowerCase().trim();
        return this.flightRepository.getAddedFlights().stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .filter(airport -> airport.toString().toString().toLowerCase().contains(pattern))
                .distinct()
                .toArray(Airport[]::new);
    }

    public PageResult<Flight> searchFlights(SearchFlightRequest flightRequest) {
        if (flightRequest.getFrom() == null || flightRequest.getTo() == null || flightRequest.getDepartureDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Every search field must be filled!");
        }
        if (flightRequest.getFrom().equals(flightRequest.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The departure and arrival airports are the same!");
        }
        List<Flight> foundFlights = this.flightRepository.getAddedFlights().stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(flightRequest.getFrom()) &&
                        flight.getTo().getAirport().equalsIgnoreCase(flightRequest.getTo()) &&
                        flight.getDepartureTime().substring(0, 10).equals(flightRequest.getDepartureDate()))
                .toList();
        return new PageResult<>(0, foundFlights.size(), foundFlights);
    }

    public Flight findFlightById(String id) {
        return flightRepository.getAddedFlights().stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No flights with this ID!"));
    }

    //// Testing client methods

    public void clearFlights() {
        this.flightRepository.getAddedFlights().clear();
    }

    //// Helper methods

    private Boolean checkIfFlightExists(Flight flightToCheck) {
        return this.flightRepository.getAddedFlights().stream()
                .anyMatch(flight ->
                        flight.getFrom().equals(flightToCheck.getFrom()) &&
                                flight.getTo().equals(flightToCheck.getTo()) &&
                                flight.getCarrier().equals(flightToCheck.getCarrier()) &&
                                flight.getDepartureTime().equals(flightToCheck.getDepartureTime()) &&
                                flight.getArrivalTime().equals(flightToCheck.getArrivalTime())
                );
    }
    public Boolean checkAirports(Airport first, Airport second) {
        return first.toString().replaceAll(" ", "").equalsIgnoreCase(second.toString().replaceAll(" ", ""));
    }

    public Boolean checkTimes(String departureTime, String arrivalTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departure = LocalDateTime.parse(departureTime, formatter);
        LocalDateTime arrival = LocalDateTime.parse(arrivalTime, formatter);
        return departure.equals(arrival) || arrival.isBefore(departure);
    }
}
