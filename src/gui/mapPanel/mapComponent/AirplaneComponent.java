package gui.mapPanel.mapComponent;

import airtraffic.utils.GeometryUtils;
import gui.Theme;
import simulation.Airplane;

import java.awt.*;
import java.awt.geom.Path2D;

public class AirplaneComponent extends SelectableComponent {
    private final Airplane airplane;
    private final Color primaryC = Theme.AIRPLANE_BG;
    private final Color secondaryC = Theme.ALERT_RED;

    // Plane silhouette in local units, nose pointing along +X, centred at (0, 0).
    // Scaled by Theme.AIRPLANE_RADIUS when drawn, so the shape is built only once.
    private static final Shape PLANE_SHAPE = createPlaneShape();

    public AirplaneComponent(Airplane airplane, Runnable onSelected) {
        super(onSelected);
        this.airplane = airplane;
    }

    public Airplane getAirplane() { return airplane; }

    public boolean isVisible() {    return airplane.getState() == Airplane.State.FLYING; }

    @Override public double getX() {
        return airplane.getX();
    }
    @Override public double getY() {
        return airplane.getY();
    }

    @Override
    public void draw(Graphics g, int sx, int sy) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (selected && blinkState){    g2.setColor(secondaryC);  }
        else{   g2.setColor(primaryC);   }

        // Screen Y grows downwards while simulation Y grows upwards,
        // so the heading must be negated to point the plane along its path.
        g2.translate(sx, sy);
        g2.rotate(-airplane.getHeading());
        g2.scale(Theme.AIRPLANE_RADIUS, Theme.AIRPLANE_RADIUS);
        g2.fill(PLANE_SHAPE);

        g2.dispose();
    }

    @Override
    public boolean contains(int sx, int sy, int px, int py) {
        return GeometryUtils.pointInCircle(px, py, sx, sy, Theme.AIRPLANE_RADIUS);
    }

    // Outline of the upper half only (nose -> wing -> tailplane -> tail),
    // mirrored below the X axis to keep the plane symmetric.
    private static Shape createPlaneShape() {
        double[][] half = {
                { 2.0, 0.00}, { 0.7, 0.35}, { 0.3, 1.30}, {-0.1, 1.30},
                { 0.1, 0.35}, {-1.2, 0.35}, {-1.3, 0.90}, {-1.7, 0.90}, {-1.7, 0.00}
        };

        Path2D.Double p = new Path2D.Double();
        p.moveTo(half[0][0], half[0][1]);
        for (int i = 1; i < half.length; i++)             p.lineTo(half[i][0],  half[i][1]);
        for (int i = half.length - 2; i >= 0; i--)        p.lineTo(half[i][0], -half[i][1]);
        p.closePath();
        return p;
    }
}
