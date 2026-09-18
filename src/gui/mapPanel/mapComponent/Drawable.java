package gui.mapPanel.mapComponent;

import java.awt.*;

public interface Drawable {
    double getX();
    double getY();

    // Draws the component; sx, sy are its position in screen pixels
    void draw(Graphics g, int sx, int sy);
    // True if the click at (px, py) hits the component drawn at (sx, sy)
    boolean contains(int sx, int sy, int px, int py);
}
