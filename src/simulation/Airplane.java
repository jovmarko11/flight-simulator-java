package simulation;

import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.utils.GeometryUtils;


public class Airplane {
    public enum State { WAITING, FLYING, LANDED }

    private final Flight flight;      // flight plan: origin, destination, departure time, duration
    private State state = State.WAITING;

    // current leg of the route:
    private double startX, startY;    // where the current leg started
    private long startSimTime;        // simulation second when the leg started
    private Airport target;           // where the plane is heading right now
    private double speed;             // simulation units per simulation second

    // current position, refreshed by update():
    private double x, y;

    public double getX() { return x; }
    public double getY() { return y; }
    public Flight getFlight() { return flight; }
    public State getState() { return state; }

    public Airplane(Flight f){
        flight = f;
        state = State.WAITING;
        startX = f.getFrom().getX();
        startY = f.getFrom().getY();
        x = startX;
        y = startY;
        target = f.getTo();
    }

    public void takeOff(long now){
        if (state != State.WAITING) return;
        state = State.FLYING;
        startSimTime = now;
        double dist = GeometryUtils.distance(x, y, target.getX(), target.getY());
        speed = dist / (flight.getDuration() * 60);
    }

    public boolean redirect(Airport newDestination, long now) {
        switch (state) {
            case FLYING -> {
                startX = x;
                startY = y;
                startSimTime = now;
                target = newDestination;
                return true;
            }
            case WAITING -> {
                target = newDestination;
                return true;
            }
            default -> {return false;}
        }
    }

    public Airport getTarget() { return target; }

    public double getHeading() {
        return Math.atan2(target.getY() - startY, target.getX() - startX);
    }

    public boolean redirectToOrigin(long now)  {   return redirect(flight.getFrom(), now);    }

    public boolean update(long now){
        // returns true if the plane has just landed
        if (state != State.FLYING) return false;

        double traveled = speed * (now - startSimTime);
        double total = GeometryUtils.distance(startX, startY, target.getX(), target.getY());

        if (traveled >= total){
            x = target.getX(); y = target.getY();
            state = State.LANDED;
            return true;
        }

        double f = traveled / total;
        x = GeometryUtils.lerp(startX, target.getX(), f);
        y = GeometryUtils.lerp(startY, target.getY(), f);
        return false;
    }

    @Override
    public String toString(){
        return "Airplane: [" + flight.getFrom() + " -> " + flight.getTo() + "]";
    }
}
