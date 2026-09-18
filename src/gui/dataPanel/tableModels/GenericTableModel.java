package gui.dataPanel.tableModels;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericTableModel<T> extends AbstractTableModel {
    protected List<T> data;
    protected String[] columnNames;

    public GenericTableModel(List<T> data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public void setData(List<T> newData){
        this.data = new ArrayList<>(newData);
        fireTableDataChanged();
    }

    public T getAt(int rowIndex){
        return data.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
}
