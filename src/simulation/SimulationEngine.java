package simulation;

import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.service.FlightService;
import simulation.interfaces.AirplaneEvent;
import simulation.interfaces.AirspaceRule;
import simulation.interfaces.ClockListener;
import simulation.interfaces.SimulationListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulationEngine implements ClockListener {
    public enum State { STOPPED, RUNNING, PAUSED }
    private State state = State.STOPPED;

    private final List<SimulationListener> listeners = new ArrayList<>();
    private final List<AirspaceRule> rules = new ArrayList<>();
    private final FlightService flightService;
    private final FlightController flightController;
    private final List<Airplane> airplanes;
    private final SimulationClock simulationClock;


    public SimulationEngine(SimulationClock sc, FlightService fs) {
        simulationClock = sc;
        flightService = fs;
        flightController = new FlightController();
        airplanes = new ArrayList<>();

        simulationClock.addListener(this);
        createAirplanes();
    }

    @Override
    public void onTick(long now) {
        launchAirplanes(now);
        updateAirplaneCoordinates(now);
        updateRules(now);
        applyRules(now);
        notifyAirplanesUpdated();
    }

    public void addListener(SimulationListener l) {     listeners.add(l);   }
    public void addRule(AirspaceRule r)       {     rules.add(r);       }
    public void removeRule(AirspaceRule r)    {     rules.remove(r);   }

    public void pause(){
        simulationClock.pause();
        state = State.PAUSED;
    }
    public void reset(){
        createAirplanes();
        flightController.reset();
        simulationClock.reset();
        state = State.STOPPED;
        resetRules();
    }
    public void start(){
        if (state == State.RUNNING) return;
        if (state == State.STOPPED){
            createAirplanes();
            flightController.reset();
            resetRules();
        }
        simulationClock.start();
        state = State.RUNNING;
    }

    public void redirectAirplane(Airplane a, Airport redirection){
        long now = simulationClock.getSimSeconds();
        if (a.redirect(redirection, now))
            notifyAirplaneEvent(a, AirplaneEvent.REDIRECTED, now);
    }
    public void redirectAirplaneToOrigin(Airplane a){
        long now = simulationClock.getSimSeconds();
        if (a.redirectToOrigin(now))
            notifyAirplaneEvent(a, AirplaneEvent.REDIRECTED,  now);
    }

    public long getSimSeconds(){
        return simulationClock.getSimSeconds();
    }
    public List<Airplane> getAirplanes(){   return new ArrayList<>(airplanes); }
    public State getState(){
        return state;
    }

    private void createAirplanes(){
        airplanes.clear();
        for (Flight f : flightService.getFlights()) {
            Airplane a = new Airplane(f);
            airplanes.add(a);
        }
        airplanes.sort(Comparator.comparing(a -> a.getFlight().getDepartureTime()));
        notifyAirplanesChanged();
    }

    private void resetRules(){
        for (AirspaceRule r : rules){
            r.reset();
        }
    }

    private void launchAirplanes(long now){
        List<Airplane> tookOff = flightController.processTakeoffs(airplanes, now);
        for (Airplane a : tookOff){
            notifyAirplaneEvent(a, AirplaneEvent.TAKEOFF, now);
        }
    }
    private void updateAirplaneCoordinates(long now){
        // refresh every plane; Airplane.update() returns true when a plane has just landed
        for (Airplane a : airplanes){
            if(a.update(now))
                notifyAirplaneEvent(a, AirplaneEvent.LANDED, now);
        }
    }
    private void updateRules(long now){
        for (AirspaceRule r : rules){
            r.onTickStart(now);
        }
    }

    private void applyRules(long now){
        for (AirspaceRule r : rules){
            for (Airplane a : airplanes){
                if (a.getState() == Airplane.State.FLYING)
                    r.apply(a, now, this);
            }
        }
    }

    private void notifyAirplanesChanged(){
        for (SimulationListener l : new ArrayList<>(listeners)){
            l.onAirplanesChanged();
        }
    }
    private void notifyAirplanesUpdated(){
        for (SimulationListener l : new ArrayList<>(listeners)){
            l.onAirplanesUpdated();
        }
    }
    private void notifyAirplaneEvent(Airplane a, AirplaneEvent event, long now){
        for (SimulationListener l : new ArrayList<>(listeners)){
            l.onAirplaneEvent(a, event, now);
        }
    }

}
