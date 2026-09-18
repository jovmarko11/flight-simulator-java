package airtraffic.utils;

import airtraffic.model.Airport;
import airtraffic.model.Flight;

import java.util.List;

public class DataBundle {
    private final List<Flight> flights;
    private final List<Airport> airports;

    public DataBundle(List<Flight> flights, List<Airport> airports) {
        this.flights = flights;
        this.airports = airports;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public List<Airport> getAirports() {
        return airports;
    }
}
