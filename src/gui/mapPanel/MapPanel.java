package gui.mapPanel;

import airtraffic.service.AirportService;
import simulation.SimulationEngine;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private final AirportService airportService;
    private final MapView mapView;
    private final AirportFilterPanel airportFilterPanel;
    private final SimulationControlPanel simulationControlPanel;

    public MapPanel(AirportService as, SimulationEngine simulationEngine, Runnable pause, Runnable resume) {
        setLayout(new BorderLayout());
        this.airportService = as;
        this.mapView = new MapView(airportService, simulationEngine, pause, resume); // takes the pause/resume hooks of the InactivityTimer
        this.airportFilterPanel = new AirportFilterPanel(airportService, mapView::setAirportVisible); // toggles which airports are shown on the map
        this.simulationControlPanel = new SimulationControlPanel(simulationEngine, pause, resume);

        add(mapView, BorderLayout.CENTER);
        add(airportFilterPanel, BorderLayout.EAST);
        add(simulationControlPanel, BorderLayout.NORTH);
    }
}
