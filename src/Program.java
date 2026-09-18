import airtraffic.service.AirportService;
import airtraffic.service.DataService;
import airtraffic.service.FlightService;
import gui.MainFrame;
import simulation.*;
import simulation.interfaces.AirplaneEvent;
import simulation.interfaces.AirspaceRule;
import simulation.interfaces.SimulationListener;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class Program {
    AirportService airportService;
    FlightService flightService;
    DataService dataService;
    SimulationClock simulationClock;
    SimulationEngine simulationEngine;

    // Console log of the simulation: "[HH:MM] EVENT      FROM -> CURRENT TARGET"
    SimulationListener informer = new SimulationListener(){
        @Override
        public void onAirplaneEvent(Airplane a, AirplaneEvent event, long now) {
            System.out.printf("[%s] %-10s %s -> %s%n",
                    SimulationClock.formatSimTime(now), event,
                    a.getFlight().getFrom().getCode(), a.getTarget().getCode());
        }
    };

    AirspaceRule fourthQuadrantBan = new AirspaceRule() {
        final Set<Airplane> handled = new HashSet<>();
        @Override
        public void apply(Airplane a, long now, SimulationEngine simulationEngine){
            if (!handled.contains(a) && a.getX() >= 0 && a.getY() <= 0){
                simulationEngine.redirectAirplaneToOrigin(a);
                handled.add(a);
                System.out.printf("[%s] %-10s %s (fourth quadrant ban)%n",
                        SimulationClock.formatSimTime(now), "RULE",
                        a.getFlight().getFrom().getCode());
            }
        }

        @Override
        public void reset() {
            handled.clear();
        }
    };

    public Program(){
        airportService = new AirportService();
        flightService = new FlightService();
        dataService = new DataService(airportService, flightService);
        simulationClock = new SimulationClock();
        simulationEngine = new SimulationEngine(simulationClock, flightService);
        simulationEngine.addListener(informer);
        //simulationEngine.addRule(fourthQuadrantBan);
    }

    public void run(){
        SwingUtilities.invokeLater(()->new MainFrame(airportService, flightService, dataService, simulationEngine).setVisible(true));
    }
}
