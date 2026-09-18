package simulation.interfaces;

import simulation.Airplane;

public interface SimulationListener {
    // The list of planes changed: the GUI has to rebuild its components
    default void onAirplanesChanged() {}

    // One simulation tick passed: plane positions moved
    default void onAirplanesUpdated() {}

    // A single plane took off, landed or was redirected
    default void onAirplaneEvent(Airplane a, AirplaneEvent event, long now)    {}
}

