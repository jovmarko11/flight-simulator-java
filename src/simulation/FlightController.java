package simulation;

import airtraffic.model.Airport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightController {
    private static final long RUNWAY_INTERVAL = 600;
    private final Map<Airport, Long> lastTakeoff = new HashMap<>();


    // returns the planes that have just taken off
    public List<Airplane> processTakeoffs(List<Airplane> airplanes, long now){
        List<Airplane> tookOff = new ArrayList<>();
        for (Airplane a : airplanes) {
            if (a.getState() != Airplane.State.WAITING) continue;

            long dep = a.getFlight().getDepartureTime().toSecondOfDay();
            if (now < dep) continue;

            Airport from = a.getFlight().getFrom();
            Long last = lastTakeoff.get(from);

            if (last != null && now - last < RUNWAY_INTERVAL) continue;

            a.takeOff(now);
            tookOff.add(a);
            lastTakeoff.put(from, now);
        }
        return tookOff;
    }

    public void reset(){
        lastTakeoff.clear();
    }
}
