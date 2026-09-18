package gui.dataPanel;

import airtraffic.exception.ValidationException;
import airtraffic.model.Airport;
import airtraffic.service.AirportService;
import airtraffic.service.DataService;
import airtraffic.utils.Validator;
import gui.Theme;
import gui.dataPanel.tableModels.AirportTableModel;
import gui.utils.ErrorDialog;

import javax.swing.*;
import java.awt.*;

public class AirportPanel extends DataPanel {
    private final AirportService airportService;
    private final DataService dataService;

    private JTextField nameField;
    private JTextField codeField;
    private JTextField xField;
    private JTextField yField;

    private AirportTableModel airportTableModel;

    public AirportPanel(AirportService as, DataService dataService){
        this.airportService = as;
        this.dataService = dataService;

        initializeComponents();
        airportService.addListener(()-> airportTableModel.setData(airportService.getAirports()));
    }

    @Override
    protected void initializeFormPanel() {
        formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(Theme.LABEL_FIELD_GAP, Theme.PANEL_PADDING, Theme.PANEL_PADDING, Theme.PANEL_PADDING));
        formPanel.setBackground(Theme.PANEL_BG);
        // labels
        JLabel nameL = new JLabel("Name");
        JLabel codeL = new JLabel("Code");
        JLabel xL = new JLabel("X coordinate");
        JLabel yL = new JLabel("Y coordinate");
        nameL.setFont(Theme.UI_FONT);
        codeL.setFont(Theme.UI_FONT);
        xL.setFont(Theme.UI_FONT);
        yL.setFont(Theme.UI_FONT);
        // TextFields
        nameField = new JTextField();
        codeField = new JTextField();
        xField = new JTextField();
        yField = new JTextField();

        formPanel.add(nameL);
        formPanel.add(nameField);
        formPanel.add(codeL);
        formPanel.add(codeField);
        formPanel.add(xL);
        formPanel.add(xField);
        formPanel.add(yL);
        formPanel.add(yField);
    }

    @Override
    protected void initializeSubmitBtn() {
        submitBtn.addActionListener(e -> {
            try{
                String name = nameField.getText();
                String code = codeField.getText();
                double x = Validator.parseDouble(xField.getText(), "X coordinate");
                double y = Validator.parseDouble(yField.getText(), "Y coordinate");
                airportService.addAirport(name, code, x, y);
                nameField.setText("");
                codeField.setText("");
                xField.setText("");
                yField.setText("");
            }
            catch(Exception ex){
                ErrorDialog.showError(this, ex.getMessage());
            }
        });
    }

    @Override
    protected void initializeDeleteBtn() {
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                Airport selected = airportTableModel.getAt(modelRow);
                try {
                    dataService.removeAirport(selected);          // through DataService, so the removal rule is checked
                } catch (ValidationException ex) {
                    ErrorDialog.showError(this, ex.getMessage());
                }
            }
        });
    }

    @Override
    protected JTable initializeTable() {
        airportTableModel = new AirportTableModel(airportService.getAirports());
        table = new JTable(airportTableModel);
        return table;
    }
}
