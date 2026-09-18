package airtraffic.service;

import airtraffic.exception.FileParseException;
import airtraffic.exception.ValidationException;
import airtraffic.files.CsvHandler;
import airtraffic.files.FileHandler;
import airtraffic.files.JsonHandler;
import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.utils.DataBundle;

import java.io.File;
import java.util.List;

public class DataService {
    private final AirportService airportService;
    private final FlightService flightService;

    private final FileHandler csvHandler = new CsvHandler();
    private final FileHandler jsonHandler = new JsonHandler();

    public DataService(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }

    private void importFrom(FileHandler fileHandler, File file, String formatName){
        DataBundle bundle = fileHandler.read(file);

        List<Airport> oldAirports = airportService.getAirports();
        List<Flight> oldFlights = flightService.getFlights();
        try{
            airportService.clearAll();
            flightService.clearAll();
            for (Airport a : bundle.getAirports()) {
                try {
                    airportService.addAirport(a.getName(), a.getCode(), a.getX(), a.getY());
                } catch (ValidationException e) {
                    throw new FileParseException("Invalid airport record in " + formatName + ": " + e.getMessage());
                }
            }

            for (Flight f : bundle.getFlights()) {
                try{
                    flightService.addFlight(f.getFrom(), f.getTo(), f.getDepartureTime(), f.getDuration());
                }
                catch(ValidationException e){
                    throw new FileParseException("Invalid flight record in " + formatName + ": " + e.getMessage());
                }
            }
        }
        catch (FileParseException | ValidationException e){
            airportService.clearAll();
            flightService.clearAll();
            for (Airport a : oldAirports) {
                airportService.addAirport(a.getName(), a.getCode(), a.getX(), a.getY());
            }
            for (Flight f : oldFlights) {
                flightService.addFlight(f.getFrom(), f.getTo(), f.getDepartureTime(), f.getDuration());
            }
            throw new FileParseException(e.getMessage() + " The existing data has been restored.");
        }
    }

    private void exportTo(FileHandler handler, File file, String formatName) throws FileParseException {
        DataBundle bundle = new DataBundle(flightService.getFlights(), airportService.getAirports());
        handler.write(file, bundle);
    }
    public void importFromCsv(File f) {     importFrom(csvHandler, f, "csv");   }
    public void importFromJson(File f) {    importFrom(jsonHandler, f, "json"); }
    public void exportToCsv(File f) throws FileParseException {     exportTo(csvHandler, f, "csv"); }
    public void exportToJson(File f) throws FileParseException {    exportTo(jsonHandler, f, "json");   }


    public void removeAirport(Airport a){
        int n = flightService.getFlightsUsing(a).size();
        if (n > 0) throw new ValidationException("Airport " + a.getCode() + " is used by " + n +
                " flight(s). Remove those flights first and try again.");
        airportService.removeAirport(a);
    }
}
