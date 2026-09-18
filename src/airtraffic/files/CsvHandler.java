package airtraffic.files;

import airtraffic.exception.FileParseException;
import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.utils.DataBundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvHandler implements FileHandler {

    @Override
    public DataBundle read(File file) throws FileParseException {
        List<Airport> airports = new ArrayList<>();
        List<Flight> flights = new ArrayList<>();

        Map<String, Airport> airportsByCode = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(file.toPath())){
            String line;

            boolean readingAirports = false;
            boolean readingFlights = false;

            while ((line = br.readLine()) != null){

                line = line.trim();

                if (line.isEmpty()) continue;

                if (line.equals("# AIRPORTS")){
                    expectedHeader(br, "CODE,NAME,X,Y");
                    readingAirports = true;
                    readingFlights = false;
                    continue;
                }

                if (line.equals("# FLIGHTS")){
                    expectedHeader(br, "FROM,TO,DEPARTURE,DURATION");
                    readingFlights = true;
                    readingAirports = false;
                    continue;
                }

                if (readingAirports) {
                    Airport airport = parseAirport(line);

                    if (airportsByCode.containsKey(airport.getCode()))
                        throw new FileParseException("Duplicate airport code: " + airport.getCode());

                    airports.add(airport);
                    airportsByCode.put(airport.getCode(), airport);
                }

                if (readingFlights){
                    Flight flight = parseFlight(line, airportsByCode);

                    flights.add(flight);
                }
            }
            return new DataBundle(flights, airports);
        }
        catch (FileParseException e){
            throw e;
        }
        catch (Exception e){
            String msg = (e.getMessage() != null) ? e.getMessage() : "Unexpected error while reading the file (" + e.getClass().getSimpleName() + ").";
            throw new FileParseException(msg);
        }
    }

    @Override
    public void write(File file, DataBundle bundle) throws FileParseException {
        try (BufferedWriter bw = Files.newBufferedWriter(file.toPath())) {
            bw.write("# AIRPORTS");
            bw.newLine();

            bw.write("CODE,NAME,X,Y");
            bw.newLine();

            for (Airport a : bundle.getAirports()) {

                bw.write(
                        a.getCode() + "," +
                                a.getName() + "," +
                                a.getX() + "," +
                                a.getY()
                );

                bw.newLine();
            }

            bw.newLine();

            bw.write("# FLIGHTS");
            bw.newLine();

            bw.write("FROM,TO,DEPARTURE,DURATION");
            bw.newLine();

            for (Flight f : bundle.getFlights()) {

                bw.write(
                        f.getFrom().getCode() + "," +
                                f.getTo().getCode() + "," +
                                f.getDepartureTime() + "," +
                                f.getDuration()
                );

                bw.newLine();
            }
        } catch (IOException e) {
            throw new FileParseException(e.getMessage());
        }
    }

    private void expectedHeader(BufferedReader br, String expected) throws IOException{
        String header = br.readLine();
        if (header == null || !header.trim().equalsIgnoreCase(expected)){
            throw new FileParseException(
                    "File does not contain the expected columns (expected: " + expected + "). " + "Check the format or select another file.");
        }
    }

}
