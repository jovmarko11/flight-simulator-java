package airtraffic.files;

import airtraffic.exception.FileParseException;
import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.utils.DataBundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHandler implements FileHandler {

    // DTO classes describing the JSON layout. They must be static so Gson can instantiate them.

    private static class JsonData {
        List<AirportJson> airports;
        List<FlightJson> flights;
    }

    private static class AirportJson {
        String code;
        String name;
        double x, y;
    }

    private static class FlightJson {
        String from;
        String to;
        String departure;
        int duration;
    }

    @Override
    public DataBundle read(File file) throws FileParseException {
        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            Gson gson = new Gson();
            JsonData data = gson.fromJson(br, JsonData.class);

            if (data == null || data.airports == null)
                throw new FileParseException("Empty or invalid JSON file: " + file.getName());

            List<Airport> airports = new ArrayList<>();
            List<Flight> flights = new ArrayList<>();
            Map<String, Airport> airportsByCode = new HashMap<>();

            // Airports: reuse parseAirport() so JSON and CSV share the same validation
            for (AirportJson a : data.airports) {
                Airport airport = parseAirport(a.code + "," + a.name + "," + a.x + "," + a.y);

                if (airportsByCode.containsKey(airport.getCode()))
                    throw new FileParseException("Duplicate airport code: " + airport.getCode());

                airportsByCode.put(airport.getCode(), airport);
                airports.add(airport);
            }

            // Flights: the section may be missing
            if (data.flights != null) {
                for (FlightJson f : data.flights) {
                    Flight flight = parseFlight(
                            f.from + "," + f.to + "," + f.departure + "," + f.duration,
                            airportsByCode);
                    flights.add(flight);
                }
            }

            return new DataBundle(flights, airports);
        } catch (FileParseException e) {
            throw e;
        } catch (Exception e) {
            String msg = (e.getMessage() != null) ? e.getMessage() : "Unexpected error while reading the JSON file (" + e.getClass().getSimpleName() + ").";
            throw new FileParseException(msg);
        }
    }

    @Override
    public void write(File file, DataBundle bundle) throws FileParseException {
        JsonData data = new JsonData();
        data.airports = new ArrayList<>();
        data.flights = new ArrayList<>();

        for (Airport a : bundle.getAirports()) {
            AirportJson aj = new AirportJson();
            aj.code = a.getCode();
            aj.name = a.getName();
            aj.x = a.getX();
            aj.y = a.getY();
            data.airports.add(aj);
        }

        for (Flight f : bundle.getFlights()) {
            FlightJson fj = new FlightJson();
            fj.from = f.getFrom().getCode();
            fj.to = f.getTo().getCode();
            fj.departure = f.getDepartureTime().toString();
            fj.duration = f.getDuration();
            data.flights.add(fj);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (BufferedWriter bw = Files.newBufferedWriter(file.toPath())) {
            gson.toJson(data, bw);
        } catch (IOException e) {
            throw new FileParseException(e.getMessage());
        }
    }
}
