package simulation;

import simulation.interfaces.ClockListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationClock {
    private static final int TICK_MS = 200;
    private int SIM_SECONDS_PER_TICK = 120;
    private final List<ClockListener> listeners = new ArrayList<>();
    private final Timer timer = new Timer(TICK_MS, e -> tick());
    private long simSeconds;

    private void tick() {
        simSeconds += SIM_SECONDS_PER_TICK;
        notifyListeners();
    }

    private void notifyListeners(){
        for (ClockListener l : new ArrayList<>(listeners)) {
            l.onTick(simSeconds);
        }
    }

    public static String formatSimTime(long now){
        int h =  ((int)(now / 3600)) % 24;
        int m = (int) (now % 3600 / 60);
        return String.format("%02d:%02d", h, m);
    }

    public void addListener(ClockListener listener) { listeners.add(listener); }

    public void reset(){
        simSeconds = 0;
        notifyListeners();
        timer.stop();
    }
    public void start(){   timer.start();  }
    public void pause(){   timer.stop(); }

    public boolean isRunning(){  return timer.isRunning();  }
    public long getSimSeconds() { return simSeconds; }

    public void setSimSecondsPerTick(int ticks){
        SIM_SECONDS_PER_TICK = ticks;
    }
}
