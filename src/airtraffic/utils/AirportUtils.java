package airtraffic.utils;

import airtraffic.model.Airport;

import java.util.List;

public class AirportUtils {
    private AirportUtils() {}

    public static Airport findByCode(List<Airport> airports, String code){
        for (Airport a : airports){
            if (a.getCode().equals(code)){
                return a;
            }
        }
        return null;
    }

    public static Airport findNearest(List<Airport> airports, double x, double y){
        Airport best = null;
        double bestDistance = Double.MAX_VALUE;
        double distance;

        for (Airport a : airports){
            distance = GeometryUtils.distanceSq(a.getX(), a.getY(), x, y);
            if (distance < bestDistance){
                bestDistance = distance;
                best = a;
            }
        }
        return best;
    }

    public static Airport findNearest(List<Airport> airports, double x, double y, Airport exclude){
        Airport best = null;
        double bestDistance = Double.MAX_VALUE;
        double distance;
        for (Airport a : airports){
            if (a.equals(exclude)) continue;
            distance = GeometryUtils.distanceSq(a.getX(), a.getY(), x, y);
            if (distance < bestDistance){
                bestDistance = distance;
                best = a;
            }
        }
        return best;
    }

}
