package gui.mapPanel.mapComponent;

import airtraffic.model.Airport;
import airtraffic.service.AirportService;
import airtraffic.utils.AirportUtils;
import airtraffic.utils.GeometryUtils;
import gui.Theme;
import gui.mapPanel.MapView;
import simulation.Airplane;
import simulation.SimulationClock;
import simulation.SimulationEngine;
import simulation.interfaces.AirspaceRule;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Hurricane implements Overlay, AirspaceRule {
    // To activate: register with mapView.addOverlay() and simulationEngine.addRule()
    private final AirportService as;
    int x, y, radius;
    private final Set<Airplane> handled = new HashSet<>();

    public Hurricane(int x, int y, int radius,  AirportService as) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.as = as;
    }

    @Override
    public void draw(Graphics g, MapView mapView) {
        int sx = (int) mapView.toScreenX(x);
        int sy = (int) mapView.toScreenY(y);
        int srx = (int) (mapView.toScreenX(radius + x) - mapView.toScreenX(x));
        int sry = (int) Math.abs(mapView.toScreenY(radius + y) - mapView.toScreenY(y));
        g.setColor(new Color(192, 57, 43, 70));
        g.fillOval(sx - srx, sy - sry, 2 * srx, 2 * sry);
        g.setColor(Theme.ALERT_RED);
        g.drawOval(sx - srx, sy - sry, 2 * srx, 2 * sry);
        g.fillOval(sx - 3, sy - 3, 6, 6);
    }

    @Override
    public void apply(Airplane a, long now, SimulationEngine simulationEngine) {
        if (!handled.contains(a)){
            if (GeometryUtils.pointInCircle(a.getX(), a.getY(), x, y, radius)) {
                Airport nearest = AirportUtils.findNearest(as.getAirports(), a.getX(), a.getY(), a.getFlight().getTo());
                simulationEngine.redirectAirplane(a, nearest);
                handled.add(a);
                System.out.printf("[%s] %-10s %s -> %s (hurricane)%n",
                        SimulationClock.formatSimTime(now), "DIVERTED",
                        a.getFlight().getFrom().getCode(), nearest.getCode());
            }
        }
    }

    @Override
    public void reset() {
        handled.clear();
    }
}
