package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.flights.domain.Airport;
import io.codelex.flightplanner.flights.domain.Flight;
import io.codelex.flightplanner.flights.requests.AddFlightRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        Flight flightToAdd = new Flight();

        Airport departureAirport = new Airport(flightRequest.getFrom().getCountry(), flightRequest.getFrom().getCity(), flightRequest.getFrom().getAirport());
        Airport arrivalAirport = new Airport(flightRequest.getTo().getCountry(), flightRequest.getTo().getCity(), flightRequest.getTo().getAirport());

        flightToAdd.setId(generateID(flightRequest));
        flightToAdd.setFrom(departureAirport);
        flightToAdd.setTo(arrivalAirport);
        flightToAdd.setCarrier(flightRequest.getCarrier());
        flightToAdd.setDepartureTime(flightRequest.getDepartureTime());
        flightToAdd.setArrivalTime(flightRequest.getArrivalTime());

        this.flightRepository.listFlights().add(flightToAdd);
        return flightToAdd;
    }

    public void clearFlights() {
        this.flightRepository.listFlights().clear();
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
}
