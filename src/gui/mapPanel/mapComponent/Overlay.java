package gui.mapPanel.mapComponent;

import gui.mapPanel.MapView;

import java.awt.*;

public interface Overlay {
    // Draws the overlay on the map; the overlay itself decides where
    void draw(Graphics g, MapView mapView);
}
