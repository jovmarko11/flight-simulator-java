package gui.dataPanel;

import airtraffic.exception.ValidationException;
import airtraffic.model.Airport;
import airtraffic.service.AirportService;
import airtraffic.service.FlightService;
import airtraffic.utils.Validator;
import gui.Theme;
import gui.dataPanel.tableModels.FlightTableModel;
import gui.utils.ErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class FlightPanel extends DataPanel {
    private final FlightService flightService;
    private final AirportService airportService;

    private JPanel routePanel;
    private JPanel timePanel;

    private JComboBox<Airport> fromAirportCombo;
    private JComboBox<Airport> toAirportCombo;
    private JTextField departureTimeField;
    private JTextField durationField;

    private FlightTableModel flightTableModel;

    public FlightPanel(FlightService flightService, AirportService airportService) {
        this.flightService = flightService;
        this.airportService = airportService;

        initializeComponents();
        flightService.addListener(()-> flightTableModel.setData(flightService.getFlights()));
        airportService.addListener(this::refreshCombos);
        refreshCombos();
    }

    @Override
    protected void initializeFormPanel() {
        formPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        initializeRoutePanel();
        initializeTimePanel();
        formPanel.add(routePanel);
        formPanel.add(timePanel);
    }

    @Override
    protected void initializeSubmitBtn() {
        submitBtn.addActionListener(e -> {
            try{
                Airport from = (Airport) fromAirportCombo.getSelectedItem();
                Airport to = (Airport) toAirportCombo.getSelectedItem();
                if (from == null || to == null) {
                    throw new ValidationException("No airports available. Add airports before creating a flight.");
                }
                LocalTime departure = Validator.validateDeparture(departureTimeField.getText());
                int duration = Validator.parseInt(durationField.getText(), "Duration");
                flightService.addFlight(from, to, departure, duration);
                departureTimeField.setText("");
                durationField.setText("");
            }
            catch (Exception ex){
                ErrorDialog.showError(this, ex.getMessage());
            }
        });
    }

    @Override
    protected void initializeDeleteBtn() {
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0){
                int modelRow = table.convertRowIndexToModel(selectedRow);
                flightService.removeFlightAt(modelRow);
            }
        });
    }

    @Override
    protected JTable initializeTable() {
        flightTableModel = new FlightTableModel(flightService.getFlights());
        table = new JTable(flightTableModel);
        return table;
    }

    private void initializeRoutePanel(){
        routePanel = new JPanel(new GridLayout(2, 2));
        routePanel.setBorder(BorderFactory.createEmptyBorder(Theme.PANEL_PADDING, Theme.PANEL_PADDING,Theme.LABEL_FIELD_GAP, Theme.PANEL_PADDING));
        routePanel.setBackground(Theme.PANEL_BG);
        JLabel fromAirportLabel = new JLabel("Departure airport");
        JLabel toAirportLabel = new JLabel("Destination airport");

        fromAirportLabel.setFont(Theme.UI_FONT);
        toAirportLabel.setFont(Theme.UI_FONT);

        fromAirportCombo = new JComboBox<>();
        toAirportCombo = new JComboBox<>();

        routePanel.add(fromAirportLabel);
        routePanel.add(fromAirportCombo);
        routePanel.add(toAirportLabel);
        routePanel.add(toAirportCombo);
    }

    private void initializeTimePanel(){
        timePanel = new JPanel(new GridLayout(2, 3));
        timePanel.setBorder(BorderFactory.createEmptyBorder(Theme.LABEL_FIELD_GAP, Theme.PANEL_PADDING, Theme.PANEL_PADDING, Theme.PANEL_PADDING));
        timePanel.setBackground(Theme.PANEL_BG);

        JLabel departureTimeLabel = new JLabel("Departure time");
        JLabel durationLabel = new JLabel("Duration");
        JLabel departureTimeUnitLabel = new JLabel("(HH:MM)");
        JLabel durationUnitLabel = new JLabel("(minutes)");
        departureTimeLabel.setFont(Theme.UI_FONT);
        durationLabel.setFont(Theme.UI_FONT);
        departureTimeUnitLabel.setFont(Theme.SMALL_FONT);
        durationUnitLabel.setFont(Theme.SMALL_FONT);

        departureTimeField = new JTextField();
        durationField = new JTextField();

        timePanel.add(departureTimeLabel);
        timePanel.add(departureTimeField);
        timePanel.add(departureTimeUnitLabel);
        timePanel.add(durationLabel);
        timePanel.add(durationField);
        timePanel.add(durationUnitLabel);
    }

    private void refreshCombos(){
        fromAirportCombo.removeAllItems();
        toAirportCombo.removeAllItems();
        for (Airport a : airportService.getAirports()) {
            fromAirportCombo.addItem(a);
            toAirportCombo.addItem(a);
        }
    }
}
