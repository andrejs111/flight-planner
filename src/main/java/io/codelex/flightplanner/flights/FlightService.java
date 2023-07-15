package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.requests.AddFlightRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class FlightService {

    FlightRepository flightRepository;
    FlightIdRepository flightIdRepository;

    public FlightService(FlightRepository flightRepository, FlightIdRepository flightIdRepository) {
        this.flightRepository = flightRepository;
        this.flightIdRepository = flightIdRepository;
    }

    public Flight fetchFlight(String id) {
        Flight flight = flightRepository.fetchFlight(id);
        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with such ID doesn't exist!");
        }
        return flight;
    }

    public Flight addFlight(AddFlightRequest flightRequest) {

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


    public void clearFlights() {
        this.flightRepository.getAddedFlights().clear();
    }

    public void deleteFlight(String id) {
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
