package simulation.interfaces;

public interface ClockListener {
    // Called on every SimulationClock tick with the current simulation time
    void onTick(long seconds);
}
