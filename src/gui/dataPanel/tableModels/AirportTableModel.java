package gui.dataPanel.tableModels;

import airtraffic.model.Airport;

import java.util.List;

public class AirportTableModel extends GenericTableModel<Airport> {
    // Table of airports
    public AirportTableModel(List<Airport> airports) {
        super(airports, new String[]{"Name", "Code", "X coordinate", "Y coordinate"});
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Airport a = data.get(rowIndex);
        return switch (columnIndex){
            case 0 -> a.getName();
            case 1 -> a.getCode();
            case 2 -> a.getX();
            case 3 -> a.getY();
            default -> throw new IndexOutOfBoundsException("AirportTableModel.getValueAt() - index out of bounds");
        };
    }

}
