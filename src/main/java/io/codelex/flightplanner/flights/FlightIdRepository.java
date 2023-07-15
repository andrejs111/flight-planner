package io.codelex.flightplanner.flights;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.List;
@Data
@Repository
public class FlightIdRepository {
    private final Integer ID_UPPER_BOUND = 1_999_999_999;
    private final Integer ID_LOWER_BOUND = 1_000_000_000;

    private final List<String> generatedIds;

    public FlightIdRepository(List<String> generatedIds) {
        this.generatedIds = generatedIds;
    }
}
