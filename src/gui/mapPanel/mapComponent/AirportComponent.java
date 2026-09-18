package gui.mapPanel.mapComponent;

import airtraffic.model.Airport;
import airtraffic.utils.GeometryUtils;
import gui.Theme;
import java.awt.*;

public class AirportComponent extends SelectableComponent {
    private final Airport airport;
    private final Color primaryC = Theme.AIRPORTCOMPONENT_BG;
    private final Color selectedC = Theme.AIRPORTCOMPONENT_SELECTED;

    public AirportComponent(Airport airport, Runnable onSelected) {
        super(onSelected);
        this.airport = airport;
    }

    @Override public double getX() {  return airport.getX();  }
    @Override public double getY() {  return airport.getY();  }

    // Receives screen pixels already converted from simulation coordinates
    @Override
    public void draw(Graphics g, int sx, int sy) {
        if (selected && blinkState){    g.setColor(selectedC);  }
        else{   g.setColor(primaryC);   }

        g.fillRect(sx - Theme.AIRPORTCOMPONENT_SIZE / 2,  sy - Theme.AIRPORTCOMPONENT_SIZE / 2, Theme.AIRPORTCOMPONENT_SIZE, Theme.AIRPORTCOMPONENT_SIZE);

        g.setColor(Theme.AIRPORTCOMPONENT_BG);
        g.drawString(airport.getCode(), (sx + Theme.AIRPORTCOMPONENT_SIZE / 2 + 4), (sy + 4));
    }

    @Override
    public boolean contains(int sx, int sy, int px, int py) {
        int half = Theme.AIRPORTCOMPONENT_SIZE / 2;
        return GeometryUtils.pointInCenteredRect(px, py, sx, sy, half, half);
    }

    public Airport getAirport() { return airport; }

}
