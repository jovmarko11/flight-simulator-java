package airtraffic.service;

import airtraffic.exception.ValidationException;
import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.utils.Validator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FlightService extends ChangeNotifier {
    private final List<Flight> flights;

    public FlightService(){
        this.flights = new ArrayList<>();
    }

    public List<Flight> getFlights() { return new ArrayList<>(flights); }

    // mutators
    public void addFlight(Airport from, Airport to, LocalTime departureTime, int duration) throws ValidationException {
        Validator.validateFlightRoute(from.getCode(), to.getCode());
        Validator.validateDuration(duration);

        Flight f = new Flight(from, to, departureTime, duration);
        flights.add(f);
        notifyListeners();
    }

    public List<Flight> getFlightsUsing(Airport a){
        List<Flight> result = new ArrayList<>();
        for (Flight f : flights){
            if (f.getFrom().equals(a) || f.getTo().equals(a)) result.add(f);
        }
        return result;
    }

    public void removeFlightAt(int index){
        if (index < 0 || index >= flights.size())
            throw new IndexOutOfBoundsException("index out of bounds");
        flights.remove(index);
        notifyListeners();
    }

    public void clearAll() {
        flights.clear();
        notifyListeners();
    }

}
