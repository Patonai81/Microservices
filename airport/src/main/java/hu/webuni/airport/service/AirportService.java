package hu.webuni.airport.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.model.QFlight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AirportService {

	private final AirportRepository airportRepository;
	private final FlightRepository flightRepository;


	@Transactional
	public Airport save(Airport airport) {
		checkUniqueIata(airport.getIata(), null);
		return airportRepository.save(airport);
	}
	
	@Transactional
	public Airport update(Airport airport) {
		checkUniqueIata(airport.getIata(), airport.getId());
		if(airportRepository.existsById(airport.getId())) {
			return airportRepository.save(airport);
		}
		else
			throw new NoSuchElementException();
	}
	
	private void checkUniqueIata(String iata, Long id) {
		
		boolean forUpdate = id != null;
		Long count = forUpdate ?
				airportRepository.countByIataAndIdNot(iata, id)
				:airportRepository.countByIata(iata);
		
		if(count > 0)
			throw new NonUniqueIataException(iata);
	}
	
	public List<Airport> findAll(){
		return airportRepository.findAll();
	}
	
	public Optional<Airport> findById(long id){
		return airportRepository.findById(id);
	}
	
	@Transactional
	public void delete(long id) {
		airportRepository.deleteById(id);
	}
	
	@Transactional
	public Flight createFlight(String flightNumber, long takeoffAirportId, long landingAirportId, LocalDateTime takeoffDateTime) {
		Flight flight = new Flight();
		flight.setFlightNumber(flightNumber);
		flight.setTakeoff(airportRepository.findById(takeoffAirportId).get());
		flight.setLanding(airportRepository.findById(landingAirportId).get());
		flight.setTakeoffTime(takeoffDateTime);
		return flightRepository.save(flight);
	}
//	
//	public List<Flight> findFlightsByExample(Flight example){
//		
//		long id = example.getId();
//		String flightNumber = example.getFlightNumber();
//		String takeoffIata = null;
//		Airport takeoff = example.getTakeoff();
//		if(takeoff != null)
//			takeoffIata  = takeoff.getIata();
//		LocalDateTime takeoffTime = example.getTakeoffTime();
//		
//		Specification<Flight> spec = Specification.where(null);
//		
//		if(id > 0) {
//			spec = spec.and(FlightSpecifications.hasId(id));
//		}
//		
//		if(StringUtils.hasText(flightNumber))
//			spec = spec.and(FlightSpecifications.hasFlightNumber(flightNumber));
//		
//		if(StringUtils.hasText(takeoffIata))
//			spec = spec.and(FlightSpecifications.hasTakoffIata(takeoffIata));
//		
//		if(takeoffTime != null)
//			spec = spec.and(FlightSpecifications.hasTakoffTime(takeoffTime));
//		
//		return flightRepository.findAll(spec, Sort.by("id"));
//	}
	
	public List<Flight> findFlightsByExample(Flight example){
		
		long id = example.getId();
		String flightNumber = example.getFlightNumber();
		String takeoffIata = null;
		Airport takeoff = example.getTakeoff();
		if(takeoff != null)
			takeoffIata  = takeoff.getIata();
		LocalDateTime takeoffTime = example.getTakeoffTime();
		
		List<Predicate> predicates = new ArrayList<>();
		QFlight flight = QFlight.flight;
		
		if(id > 0)
			predicates.add(flight.id.eq(id));
		
		if(StringUtils.hasText(flightNumber))
			predicates.add(flight.flightNumber.startsWithIgnoreCase(flightNumber));
		
		if(StringUtils.hasText(takeoffIata))
			predicates.add(flight.takeoff.iata.startsWith(takeoffIata));
		
		if(takeoffTime != null) {
			LocalDateTime startOfDay = LocalDateTime.of(takeoffTime.toLocalDate(), LocalTime.MIDNIGHT);
			predicates.add(flight.takeoffTime.between(startOfDay, startOfDay.plusDays(1)));
		}
		return Lists.newArrayList(flightRepository.findAll(ExpressionUtils.allOf(predicates), Sort.by("id")));
	}
}
