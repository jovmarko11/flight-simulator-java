package airtraffic.service;

import airtraffic.model.Airport;
import airtraffic.utils.AirportUtils;
import airtraffic.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class AirportService extends ChangeNotifier {
    private final List<Airport> airports;

    public AirportService() {
        super();
        this.airports = new ArrayList<>();
    }

    public List<Airport> getAirports() { return new ArrayList<>(airports); }


    // mutators
    public void addAirport(String name, String code, double x, double y){
        List<String> existingCodes = new ArrayList<>();
        for (Airport airport : airports) {
            existingCodes.add(airport.getCode());
        }

        // validation
        Validator.validateAirportCode(code);
        Validator.validateCodeUnique(code, existingCodes);
        Validator.validateAirportName(name);
        Validator.validateCoordinates(x, y);

        // insert
        airports.add(new Airport(name, code, x, y));
        notifyListeners();
    }

    public void removeAirport(Airport a){
        if (airports.remove(a))
            notifyListeners();
    }

    public void clearAll (){
        airports.clear();
        notifyListeners();
    }

    // utils

    public Airport searchAirportByCode(String code){
        return AirportUtils.findByCode(airports, code);
    }

    public boolean checkExistance(String code){
        for (Airport a : airports){
            if (a.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }
}
