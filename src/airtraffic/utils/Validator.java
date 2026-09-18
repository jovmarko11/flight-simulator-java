package airtraffic.utils;

import airtraffic.exception.ValidationException;

import java.time.LocalTime;
import java.util.List;

public class Validator {

    private Validator() {}

    public static void validateAirport(String name, String code, double x, double y) {
        validateAirportCode(code);
        validateAirportName(name);
        validateCoordinates(x, y);
    }

    public static void validateAirportCode(String code){
        if (code == null || code.isBlank()){
            throw new ValidationException("Airport code is null or empty. Please enter a valid airport code.");
        }

        if (!code.matches("^[A-Z]{3}$")){
            throw new ValidationException(
                    "Invalid airport code: \"" + code + "\". " + "Code must be exactly 3 uppercase letters.");
        }
    }

    public static void validateCodeUnique(String code, List<String> existingCodes) throws ValidationException{
        if (existingCodes.contains(code)){
            throw new ValidationException("Airport code \"" + code + "\" already exists.");
        }
    }

    public static void validateAirportName(String name) throws ValidationException {
        if (name == null || name.isBlank()){
            throw new ValidationException("Airport can not be empty or null.");
        }
    }

    public static void validateCoordinates(double x, double y) throws ValidationException {
        if (x < -180 || x > 180){
            throw new ValidationException("Invalid X coordinate: " + x + " (must be in range [-180, 180]).");
        }
        if (y < -90 || y > 90){
            throw new ValidationException("Invalid Y coordinate: " + y + " (must be in range [-90, 90]).");
        }
    }

    // Flight validation

    public static LocalTime validateFlight(String fromCode, String toCode, String departure, int duration) throws ValidationException {
        validateFlightRoute(fromCode, toCode);
        LocalTime departureTime = validateDeparture(departure);
        validateDuration(duration);
        return departureTime;
    }

    public static LocalTime validateDeparture(String time) throws ValidationException {
        if (time == null || time.isBlank())
            throw new ValidationException("Departure time cannot be empty. Please enter time in HH:mm format (e.g. 08:30).");

        if (!time.matches("([01]\\d|2[0-3]):[0-5]\\d"))
            throw new ValidationException("Invalid departure time: \"" + time + "\". " +
                    "Please use HH:mm format with a valid 24-hour time (e.g. 08:30, 23:59).");
        return LocalTime.parse(time);
    }

    public static void validateDuration(int duration) throws ValidationException {
        if (duration <= 0)
            throw new ValidationException("Flight duration must be a positive number of minutes (e.g. 90). " +
                    "Entered value: " + duration + ".");
    }

    public static void validateFlightRoute(String fromCode, String toCode) throws ValidationException {
        if (fromCode != null && fromCode.equals(toCode))
            throw new ValidationException("Departure and arrival airport cannot be the same (\"" + fromCode + "\"). " +
                    "Please select two different airports.");
    }

    // Value parsing

    public static double parseDouble(String value, String fieldName)
            throws ValidationException {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("\"" + value + "\" is not a valid number for " + fieldName + ". " + "Please enter a numeric value (e.g. 10.5).");
        }
    }

    public static int parseInt(String value, String fieldName)
            throws ValidationException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("\"" + value + "\" is not a valid integer for " + fieldName + ". " + "Please enter a whole number (e.g. 90).");
        }
    }

}