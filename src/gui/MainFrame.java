package gui;

import airtraffic.service.AirportService;
import airtraffic.service.DataService;
import airtraffic.service.FlightService;
import gui.dataPanel.AirportPanel;
import gui.dataPanel.FlightPanel;
import gui.mapPanel.MapPanel;
import gui.utils.InactivityTimer;
import simulation.SimulationEngine;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final AirportService airportService;
    private final FlightService flightService;
    private final DataService dataService;
    private final SimulationEngine simulationEngine;

    private AirportPanel airportPanel;
    private FlightPanel flightPanel;
    private MapPanel mapPanel;

    private JTabbedPane tabbedPane;
    private FileMenuBar menuBar;

    private final InactivityTimer inactivityTimer;

    public MainFrame(AirportService as, FlightService fs, DataService ds, SimulationEngine se){
        airportService = as;
        flightService = fs;
        simulationEngine = se;
        dataService = ds;

        configureMainFrame();
        inactivityTimer = new InactivityTimer(this);
        initializeComponents();
    }

    private void initializeComponents(){
        menuBar = new FileMenuBar(dataService);
        airportPanel = new AirportPanel(airportService, dataService);
        flightPanel = new FlightPanel(flightService, airportService);
        mapPanel = new MapPanel(airportService, simulationEngine, inactivityTimer::pause, inactivityTimer::resume);

        initializeTabbedPane();

        add(tabbedPane,  BorderLayout.CENTER);
        setJMenuBar(menuBar);
    }

    private void configureMainFrame(){
        setTitle("Air Traffic Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeTabbedPane(){
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Airports", airportPanel);
        tabbedPane.addTab("Flights", flightPanel);
        tabbedPane.addTab("Map", mapPanel);

        for (int i = 0; i < tabbedPane.getTabCount(); i++){
            JLabel tabLabel = new JLabel(tabbedPane.getTitleAt(i), SwingConstants.CENTER);
            tabLabel.setFont(Theme.UI_FONT);
            tabLabel.setForeground(Theme.MIDNIGHT_NAVY);
            tabLabel.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            tabbedPane.setTabComponentAt(i, tabLabel);
        }
    }
}
