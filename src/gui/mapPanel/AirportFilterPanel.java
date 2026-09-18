package gui.mapPanel;

import airtraffic.model.Airport;
import airtraffic.service.AirportService;
import gui.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

public class AirportFilterPanel extends JPanel {
    private final AirportService airportService;

    private JScrollPane scrollPane;
    private JPanel checkBoxPanel;

    private final BiConsumer<Airport, Boolean> onVisibilityChange;


    public AirportFilterPanel(AirportService airportService, BiConsumer<Airport, Boolean> onVisibilityChange) {
        this.airportService = airportService;
        this.onVisibilityChange = onVisibilityChange;

        setLayout(new BorderLayout());
        setBackground(Theme.MIDNIGHT_NAVY);
        setPreferredSize(new Dimension(250, 0));

        airportService.addListener(() -> buildAirports(airportService.getAirports()));

        initializeComponents();
        buildAirports(airportService.getAirports());
    }

    private void buildAirports(List<Airport> airports){
        checkBoxPanel.removeAll();
        for (Airport airport : airports){
            JCheckBox cb = new JCheckBox(airport.fullName(), true);
            cb.setForeground(Theme.BTN_FG);
            cb.addActionListener(e -> onVisibilityChange.accept(airport, cb.isSelected()));
            checkBoxPanel.add(cb);
        }
        revalidate();
        repaint();
    }

    private void initializeComponents(){
        initializeHeader();
        initializeScrollPane();
    }

    private void initializeHeader(){
        JLabel title = new JLabel ("Filter");
        title.setFont(Theme.UI_FONT);
        title.setForeground(Theme.TABLE_HEADER_FG);
        title.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 0));
        add(title, BorderLayout.NORTH);
    }

    private void initializeScrollPane(){
        checkBoxPanel = new JPanel();
        checkBoxPanel.setBackground(Theme.MIDNIGHT_NAVY);
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(checkBoxPanel);
        scrollPane.getViewport().setBackground(Theme.MIDNIGHT_NAVY);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
}
