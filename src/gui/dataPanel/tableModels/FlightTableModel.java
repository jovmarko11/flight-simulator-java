package gui.dataPanel.tableModels;

import airtraffic.model.Flight;

import java.util.List;

public class FlightTableModel extends GenericTableModel<Flight> {
    // Table of flights
    public FlightTableModel(List<Flight> flights) {
        super(flights, new String[]{"From", "To", "Departure", "Arrival", "Duration"});
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Flight f = data.get(rowIndex);
        return switch(columnIndex){
            case 0 -> f.getFrom();
            case 1 -> f.getTo();
            case 2 -> f.getDepartureTime();
            case 3 -> f.getArrivalTime();
            case 4 -> f.getDuration();
            default -> throw new IndexOutOfBoundsException("FlightTableModel.getValueAt() - index out of bounds");
        };
    }

}
