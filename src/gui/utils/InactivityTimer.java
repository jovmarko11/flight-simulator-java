package gui.utils;

import javax.swing.*;
import java.awt.*;

public class InactivityTimer {
    private static final long EXIT = 60;
    private static final long WARNING = 5;
    private long lastActivity;
    private final InactivityDialog inactivityDialog;

    private int pauseCount = 0;
    private final Timer timer;

    public InactivityTimer(Frame owner){
        timer = new Timer(1000, e -> tick());

        inactivityDialog = new InactivityDialog(owner, () ->
                lastActivity = System.currentTimeMillis());

        Toolkit.getDefaultToolkit().addAWTEventListener(e ->
                lastActivity = System.currentTimeMillis(),
                AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);

        lastActivity = System.currentTimeMillis();
        timer.start();
    }

    private void tick(){
        long remaining = EXIT - (System.currentTimeMillis() - lastActivity) / 1000;
        if (remaining > WARNING){
            if (inactivityDialog.isVisible())
                inactivityDialog.setVisible(false);
        }
        else if (remaining > 0){
            inactivityDialog.showCountdown(remaining);
        }
        else {
            System.exit(0);
        }
    }

    public void pause()  { if (++pauseCount == 1) timer.stop(); }

    public void resume() {
        if (pauseCount == 0) return;   // ignore unbalanced resume, the counter must stay >= 0
        if (--pauseCount == 0) {
            lastActivity = System.currentTimeMillis();
            timer.start();
        }
    }

}
