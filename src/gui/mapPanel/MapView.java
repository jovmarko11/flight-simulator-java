package gui.mapPanel;
import airtraffic.model.Airport;
import airtraffic.service.AirportService;
import gui.Theme;
import gui.mapPanel.mapComponent.*;
import simulation.Airplane;
import simulation.SimulationEngine;
import simulation.interfaces.SimulationListener;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MapView extends JPanel {
    private final AirportService airportService;
    private final SimulationEngine simulationEngine;

    private final List<AirportComponent> airportComponents;
    private final List<AirplaneComponent> airplaneComponents;
    private final List<Overlay> overlays;

    private AirportComponent selAirport;
    private AirplaneComponent selAirplane;

    private final Runnable onSelect;
    private final Runnable onDeselect;

    // Half of the visible range in simulation units: the screen shows [-ZX, ZX] x [-ZY, ZY].
    // Smaller ZX/ZY means a larger zoom, i.e. less of the world is visible.
    private int ZX = 180;
    private int ZY = 90;

    private static final int MIN_ZX = 20,  MAX_ZX = 180;
    private static final int MIN_ZY = 10,  MAX_ZY = 90;
    private static final int STEP_X = 10,  STEP_Y = 5;

    public MapView(AirportService as, SimulationEngine se, Runnable pause, Runnable resume) {
        setBackground(Theme.WINDOW_BG);
        this.airportService = as;
        simulationEngine = se;
        onSelect = pause;
        onDeselect = resume;
        airportComponents = new ArrayList<>();
        airplaneComponents = new ArrayList<>();
        overlays = new ArrayList<>();
        airportService.addListener(() -> buildAirportComponents(airportService.getAirports()));
        simulationEngine.addListener(new SimulationListener() {
            @Override
            public void onAirplanesUpdated() {
                repaint();
            }

            @Override
            public void onAirplanesChanged() {
                buildAirplaneComponents(simulationEngine.getAirplanes());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });

        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                zoomIn();
            } else {
                zoomOut();
            }
        });
        buildAirportComponents(airportService.getAirports());
        buildAirplaneComponents(simulationEngine.getAirplanes());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawOverlays(g);
        drawAirports(g);
        drawAirplanes(g);
    }

    //      ----- Drawing -----
    private void drawGrid(Graphics g) {
        g.setColor(Theme.SOFT_SKY);
        // Lines sit on round coordinates (multiples of 20); the ones outside
        // the viewport are harmlessly drawn past the panel bounds.
        for (int lon = -180; lon <= 180; lon += 20) {
            int x = (int) toScreenX(lon);
            g.drawLine(x, 0, x, getHeight());
        }
        for (int lat = -80; lat <= 80; lat += 20) {
            int y = (int) toScreenY(lat);
            g.drawLine(0, y, getWidth(), y);
        }
    }

    private void drawAirports(Graphics g) {
        for (AirportComponent ac : airportComponents) {
            if (ac.isVisible())
                ac.draw(g, (int) toScreenX(ac.getX()), (int) toScreenY(ac.getY()));
        }
    }

    private void drawAirplanes(Graphics g) {
        for (AirplaneComponent ac : airplaneComponents) {
            if (ac.isVisible())
                ac.draw(g, (int) toScreenX(ac.getX()), (int) toScreenY(ac.getY()));
        }
    }

    private void drawOverlays(Graphics g) {
        for (Overlay overlay : overlays) {
            overlay.draw(g, this);
        }
    }

    public void addOverlay(Overlay overlay) {
        overlays.add(overlay);
        repaint();
    }

    public void removeOverlay(Overlay overlay) {
        overlays.remove(overlay);
        repaint();
    }

    //      ----- Building the components -----
    private void buildAirportComponents(List<Airport> airports) {
        clearAirportSelection();
        airportComponents.clear();
        for (Airport a : airports) {
            AirportComponent ac = new AirportComponent(a, this::repaint);
            airportComponents.add(ac);
        }
        repaint();
    }

    private void buildAirplaneComponents(List<Airplane> airplanes) {
        clearAirplaneSelection();
        airplaneComponents.clear();
        for (Airplane a : airplanes) {
            AirplaneComponent ac = new AirplaneComponent(a, this::repaint);
            airplaneComponents.add(ac);
        }
        repaint();
    }

    public void setAirportVisible(Airport a, boolean visible) {
        // Called by AirportFilterPanel when a checkbox is toggled
        for (AirportComponent ac : airportComponents) {
            if (ac.getAirport().equals(a)) {
                if (!visible && ac == selAirport) clearAirportSelection();
                ac.setVisible(visible);
                break;
            }
        }
        repaint();
    }

    //      ----- Coordinate transforms -----
    // Simulation units -> pixels, used when drawing:
    // the range [-ZX, ZX] is mapped linearly onto [0, width].
    public double toScreenX(double x) {
        return (x + ZX) / (2.0 * ZX) * getWidth();
    }

    public double toScreenY(double y) {
        return (1 - (y + ZY) / (2.0 * ZY)) * getHeight();
    }

    // Pixels -> simulation units, used for mouse input
    public double fromScreenX(double sx) {
        return sx / getWidth() * 2.0 * ZX - ZX;
    }

    public double fromScreenY(double sy) {
        return (1 - sy / getHeight()) * 2.0 * ZY - ZY;
    }


    //      ----- Selection -----
    private void select(AirportComponent s) {
        selAirport = s;
        s.setSelected(true);
        selAirport.activate();
        onSelect.run();
        repaint();
    }

    private void select(AirplaneComponent a) {
        selAirplane = a;
        a.setSelected(true);
        selAirplane.activate();
        onSelect.run();
        repaint();
    }

    private void clearAirportSelection() {
        if (selAirport == null) return;
        selAirport.deactivate();
        selAirport.setSelected(false);
        selAirport = null;
        onDeselect.run();
        repaint();
    }

    private void clearAirplaneSelection(){
        if (selAirplane == null) return;
        selAirplane.deactivate();
        selAirplane.setSelected(false);
        selAirplane = null;
        onDeselect.run();
        repaint();
    }

    //      ----- Mouse handling -----
    private void handleClick(int x, int y) {
        for (AirportComponent ac : airportComponents) {
            if (!ac.isVisible()) continue;
            if (ac.contains((int) toScreenX(ac.getX()), (int) toScreenY(ac.getY()), x, y)) {
                if (ac == selAirport) clearAirportSelection();
                else {
                    select(ac);
                    break;
                }
            }
        }
        for (AirplaneComponent ac : airplaneComponents) {
            if (ac.contains((int) toScreenX(ac.getX()), (int) toScreenY(ac.getY()), x, y)) {
                if (ac == selAirplane) clearAirplaneSelection();
                else {
                    select(ac);
                    break;
                }
            }
        }

        // A selected plane together with a selected airport means: redirect the plane there.
        if (selAirport != null && selAirplane != null) {
            simulationEngine.redirectAirplane(selAirplane.getAirplane(), selAirport.getAirport());
            clearAirplaneSelection();
            clearAirportSelection();
        }
    }

    //      ----- Zoom, centred on (0, 0) -----
    // Zoom in: narrow the visible range
    private void zoomIn() {
        if (ZX - STEP_X >= MIN_ZX && ZY - STEP_Y >= MIN_ZY) {
            ZX -= STEP_X;
            ZY -= STEP_Y;
            repaint();
        }
    }

    // Zoom out: widen the visible range, up to the whole world
    private void zoomOut() {
        if (ZX + STEP_X <= MAX_ZX && ZY + STEP_Y <= MAX_ZY) {
            ZX += STEP_X;
            ZY += STEP_Y;
            repaint();
        }
    }
}