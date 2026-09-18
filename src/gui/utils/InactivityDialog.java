package gui.utils;

import javax.swing.*;
import java.awt.*;

public class InactivityDialog extends JDialog {
    private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);

    public InactivityDialog(Frame owner, Runnable onStay){
        super(owner, "Warning", false);

        JButton stayBtn = new JButton("Stay");
        stayBtn.addActionListener(e -> {
            onStay.run();
            setVisible(false);
        });

        setLayout(new BorderLayout(8, 8));
        add(messageLabel, BorderLayout.CENTER);
        add(stayBtn, BorderLayout.SOUTH);
        setSize(280, 120);
        setLocationRelativeTo(owner);
    }

    public void showCountdown(long secs) {
        messageLabel.setText("Closing in " + secs + " s");
        if (!isVisible()) setVisible(true);
    }
}
