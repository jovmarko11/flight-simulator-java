package simulation.interfaces;

import simulation.Airplane;
import simulation.SimulationEngine;
// A rule the SimulationEngine applies to the airspace on every tick
public interface AirspaceRule {
    // Applied to every flying plane, once per tick
    void apply(Airplane a, long now, SimulationEngine simulationEngine);

    // Called once per tick, before apply(), so the rule can update its own state
    default void onTickStart(long now) {}

    // Restores the rule to its initial state
    default void reset() {}
}
