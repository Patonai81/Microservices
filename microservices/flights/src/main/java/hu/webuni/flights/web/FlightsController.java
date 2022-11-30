package hu.webuni.flights.web;

import hu.webuni.flights.api.FlightsApi;
import hu.webuni.flights.dto.Airline;
import hu.webuni.flights.service.AirlineService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FlightsController implements FlightsApi {

    @Autowired
    private AirlineService airlineService;

    @PreAuthorize("hasAuthority('search')")
    @Override
    public List<Airline> searchFlight(String from, String to) {
        return airlineService.search(from, to);
    }
}
