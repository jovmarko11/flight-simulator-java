package airtraffic.service;

public interface DataChangeListener {
    // Called whenever airports or flights change, so listeners can refresh
    void onDataChanged();
}
