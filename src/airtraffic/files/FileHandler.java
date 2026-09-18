package airtraffic.files;

import airtraffic.exception.FileParseException;
import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.utils.DataBundle;
import airtraffic.utils.Validator;

import java.io.File;
import java.time.LocalTime;
import java.util.Map;

public interface FileHandler {
    DataBundle read(File file) throws FileParseException;

    void write(File file, DataBundle bundle) throws FileParseException;

     default Airport parseAirport(String line){
        String[] parts = line.split(",");

        if (parts.length != 4)
            throw new FileParseException("Invalid airport record: " + line);

        String code = parts[0].trim();
        String name = parts[1].trim();

        double x = Validator.parseDouble(parts[2], "X coordinate");
        double y = Validator.parseDouble(parts[3], "Y coordinate");

        Validator.validateAirport(name, code, x, y);

        return new Airport(name, code, x, y);
    }

    default Flight parseFlight(String line, Map<String, Airport> airportsByCode){
        String[] parts = line.split(",");

        if (parts.length != 4)
            throw new FileParseException("Invalid flight record: " + line);

        Airport from = airportsByCode.get(parts[0].trim());
        Airport to = airportsByCode.get(parts[1].trim());

        if (from == null || to == null)
            throw new FileParseException("Invalid flight record: " + line);

        int duration = Validator.parseInt(parts[3], "duration");

        LocalTime departure = Validator.validateFlight(from.getCode(), to.getCode(), parts[2], duration);
        return new Flight(from, to, departure, duration);
    }


}
