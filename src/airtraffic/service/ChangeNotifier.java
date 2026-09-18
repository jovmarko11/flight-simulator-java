package airtraffic.service;

import java.util.ArrayList;
import java.util.List;

public abstract class ChangeNotifier {
    private final List<DataChangeListener> listeners;

    public ChangeNotifier(){
        listeners = new ArrayList<DataChangeListener>();
    }

    protected void notifyListeners(){
        for (DataChangeListener listener : new ArrayList<>(listeners)) {
            listener.onDataChanged();
        }
    }

    public void addListener(DataChangeListener listener) {  listeners.add(listener);    }
    public void removeListener(DataChangeListener listener) {   listeners.remove(listener); }

}
