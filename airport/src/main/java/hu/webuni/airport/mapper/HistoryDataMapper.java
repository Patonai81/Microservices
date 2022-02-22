package hu.webuni.airport.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.airport.api.model.FlightDto;
import hu.webuni.airport.api.model.HistoryDataAirportDto;
import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.model.HistoryData;

@Mapper(componentModel = "spring")
public interface HistoryDataMapper {

	HistoryDataAirportDto airportHistoryDataToDto(HistoryData<Airport> hd);
	
	@Mapping(target="takeoff", ignore=true)
	@Mapping(target="landing", ignore=true)
	FlightDto flightToDto(Flight flight);
}
