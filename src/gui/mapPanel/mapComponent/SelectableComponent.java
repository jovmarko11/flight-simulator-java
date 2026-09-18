package gui.mapPanel.mapComponent;

import javax.swing.*;

public abstract class SelectableComponent implements Drawable {
    protected boolean selected;
    protected boolean blinkState;
    protected boolean visible = true;
    protected final Timer blinkTimer;

    protected final Runnable onStateChanged;

    public SelectableComponent(Runnable onStateChanged){
        this.onStateChanged = onStateChanged;
        blinkTimer = new Timer(500, e ->{
            blinkState = !blinkState;
            onStateChanged.run();
        });
    }

    public void activate() {    blinkTimer.start();     }
    public void deactivate() { blinkTimer.stop(); }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (!selected) blinkState = false;
    }
    public boolean isSelected() { return selected; }

    public void setVisible(boolean v){   visible = v;     }
    public boolean isVisible(){ return visible; }

}
