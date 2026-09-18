package airtraffic.model;

import java.time.LocalTime;


public class Flight {
    private Airport from;
    private Airport to;
    private LocalTime departureTime;
    private int duration;

    public Flight(Airport from, Airport to, LocalTime time, int duration) {
        this.from = from;
        this.to = to;
        this.departureTime = time;
        this.duration = duration;
    }

    public Airport getFrom() { return from; }
    public void setFrom(Airport from) { this.from = from; }

    public Airport getTo() { return to; }
    public void setTo(Airport to) { this.to = to; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public LocalTime getArrivalTime() { return departureTime.plusMinutes(duration); }

    @Override
    public String toString() {
        return from.getCode() + " -> " + to.getCode() + " duration: " + duration;
    }

}
