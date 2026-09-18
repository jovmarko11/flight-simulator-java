package gui;

import javax.swing.*;
import java.awt.*;

public class Theme {
    private Theme() { }

    /* ----------------------------- COLORS ---------------------------- */
    public static final Color MIDNIGHT_NAVY = new Color(27, 42, 74);
    public static final Color SOFT_SKY = new Color(232, 238, 247);
    public static final Color HORIZON_BLUE = new Color(74, 144, 196);
    public static final Color LIGHT_GRAY = new Color(244, 244, 244);
    public static final Color ALERT_RED = new Color(192, 57, 43);
    public static final Color FIELD_BORDER = new Color(208, 215, 227);
    public static final Color GRAY = new Color(154, 160, 170);

    public static final Color HURRICANE = new Color(154, 160, 170, 90);

    /* ------------------------ SEMANTIC ROLES ------------------------- */
    public static final Color WINDOW_BG = LIGHT_GRAY;
    public static final Color PANEL_BG = SOFT_SKY;
    public static final Color PRIMARY_BTN_BG = MIDNIGHT_NAVY;
    public static final Color DANGER_BTN_BG = ALERT_RED;
    public static final Color BTN_FG = Color.WHITE;
    public static final Color AIRPLANE_BG = HORIZON_BLUE;
    public static final Color GRID = LIGHT_GRAY;


    public static final Color TABLE_HEADER_BG = MIDNIGHT_NAVY;
    public static final Color TABLE_HEADER_FG = Color.WHITE;
    public static final Color TABLE_GRID = FIELD_BORDER;
    public static final Color SELECTION_BG = SOFT_SKY;
    public static final Color SELECTION_FG = MIDNIGHT_NAVY;

    /* ----------------------------- FONTS ----------------------------- */
    public static final Font TITLE_FONT = new Font("Georgia", Font.PLAIN, 20); // window title
    public static final Font HEADING_FONT = new Font("Georgia", Font.PLAIN, 20); // panel headings
    public static final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 14); // labels, buttons, fields
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    /* --------------------- SIZES AND SPACING ------------------------- */
    public static final int BUTTON_HEIGHT = 32;
    public static final int ROW_HEIGHT = 28;
    public static final int PANEL_PADDING = 16;
    public static final int FORM_GAP = 12;
    public static final int LABEL_FIELD_GAP = 4;
    public static final int BUTTON_GAP = 8;
    public static final int AIRPLANE_RADIUS = 4;

    /* ----------------------- BUTTON STYLE --------------------- */
    public static void styleButton(JButton btn, Color bg){
        btn.setBackground(bg);
        btn.setForeground(BTN_FG);
        btn.setFont(UI_FONT);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(80, BUTTON_HEIGHT));
    }

    /* ----------------------- Table STYLE --------------------- */
    public static void styleTable(JTable table){
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
        table.getTableHeader().setForeground(TABLE_HEADER_FG);
        table.getTableHeader().setFont(SMALL_FONT);
        table.setFont(SMALL_FONT);
        table.setSelectionBackground(SELECTION_BG);
        table.setSelectionForeground(SELECTION_FG);
        table.setRowHeight(ROW_HEIGHT);
        table.setGridColor(TABLE_GRID);
    }

    /* ----------------------- AirportComponent STYLE --------------------- */
    public static final int AIRPORTCOMPONENT_SIZE = 12;
    public static final Color AIRPORTCOMPONENT_BG = MIDNIGHT_NAVY;
    public static final Color AIRPORTCOMPONENT_SELECTED = ALERT_RED;

    /* ================= SimulationControlPanel STYLE ================= */

    /* --- buttons --- */
    public static final Dimension CONTROL_BTN_SIZE = new Dimension(104, 38);
    public static final Color SIM_START_BG = MIDNIGHT_NAVY;   // primary action
    public static final Color SIM_PAUSE_BG = HORIZON_BLUE;    // secondary action
    public static final Color SIM_RESET_BG = ALERT_RED;       // clears the state
    public static final Color CONTROL_BTN_DISABLED_BG = new Color(223, 228, 237);
    public static final Color CONTROL_BTN_DISABLED_FG = new Color(154, 160, 170);


    /* --- fonts --- */
    public static final Font SIM_HEADER_FONT = new Font("Segoe UI", Font.PLAIN, 11); // "SIMULATION TIME"
    public static final Font SIM_TIMER_FONT  = new Font("Segoe UI", Font.BOLD, 30);  // "HH:MM"
    public static final Font SIM_STATUS_FONT = new Font("Segoe UI", Font.BOLD, 13);  // status tekst

    /* --- text colors --- */
    public static final Color SIM_HEADER_FG = new Color(136, 135, 128); // muted gray
    public static final Color SIM_TIMER_FG  = MIDNIGHT_NAVY;

    /* --- status (label + dot) --- */
    public static final int   STATUS_DOT_SIZE      = 10;
    public static final Color STATUS_STOPPED_COLOR = new Color(95, 94, 90); // gray
    public static final Color STATUS_RUNNING_COLOR = HORIZON_BLUE;          // blue
    public static final Color STATUS_PAUSED_COLOR  = new Color(95, 94, 90); // gray

    /* standard control panel button */
    public static void styleControlButton(JButton btn, Color bg){
        btn.setBackground(bg);
        btn.setForeground(BTN_FG);
        btn.setFont(UI_FONT);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(CONTROL_BTN_SIZE);
    }

    /* enable/disable a button, graying it out */
    public static void setControlButtonEnabled(JButton btn, boolean enabled, Color activeBg){
        btn.setEnabled(enabled);
        btn.setBackground(enabled ? activeBg : CONTROL_BTN_DISABLED_BG);
        btn.setForeground(enabled ? BTN_FG : CONTROL_BTN_DISABLED_FG);
    }

    /* dot next to the status label, used as a JLabel icon */
    public static Icon statusDot(Color color){
        return new Icon() {
            @Override public int getIconWidth()  { return STATUS_DOT_SIZE; }
            @Override public int getIconHeight() { return STATUS_DOT_SIZE; }
            @Override public void paintIcon(Component c, Graphics g, int x, int y){
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(x, y, STATUS_DOT_SIZE, STATUS_DOT_SIZE);
                g2.dispose();
            }
        };
    }
}
