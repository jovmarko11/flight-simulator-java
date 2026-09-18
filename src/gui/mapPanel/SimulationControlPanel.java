package gui.mapPanel;

import gui.Theme;
import simulation.SimulationClock;
import simulation.SimulationEngine;
import simulation.interfaces.SimulationListener;

import javax.swing.*;
import java.awt.*;

public class SimulationControlPanel extends JPanel{
    private final SimulationEngine simulationEngine;

    Runnable onStart;
    Runnable onPause;

    private JPanel btnPanel;
    private JPanel infoPanel;

    private JLabel timeLabel;
    private JLabel statusLabel;

    private JButton startBtn;
    private JButton pauseBtn;
    private JButton resetBtn;

    public SimulationControlPanel(SimulationEngine se, Runnable pause, Runnable resume) {
        setLayout(new BorderLayout());
        onPause = pause;
        onStart = resume;
        simulationEngine = se;
        simulationEngine.addListener(new SimulationListener(){
            @Override
            public void onAirplanesUpdated() {
                refresh();
            }
        });
        setBackground(Theme.PANEL_BG);
        setBorder(BorderFactory.createEmptyBorder(10, Theme.PANEL_PADDING, 5, Theme.PANEL_PADDING));

        initializeButtonPanel();
        initializeTimerPanel();

        this.add(infoPanel, BorderLayout.WEST);
        this.add(btnPanel, BorderLayout.EAST);
        refresh();
    }

    private void initializeButtonPanel(){
        btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, Theme.BUTTON_GAP, 0));
        btnPanel.setBackground(Theme.PANEL_BG);

        initializeStartBtn();
        initializePauseBtn();
        initializeResetBtn();

        btnPanel.add(startBtn);
        btnPanel.add(pauseBtn);
        btnPanel.add(resetBtn);
    }

    private void initializeTimerPanel(){
        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setBackground(Theme.PANEL_BG);

        JLabel headerL = new JLabel("SIMULATION TIME");
        headerL.setForeground(Theme.SIM_HEADER_FG);
        headerL.setFont(Theme.SIM_HEADER_FONT);

        timeLabel = new JLabel(SimulationClock.formatSimTime(0));
        timeLabel.setForeground(Theme.SIM_TIMER_FG);
        timeLabel.setFont(Theme.SIM_TIMER_FONT);

        statusLabel = new JLabel("Stopped");
        statusLabel.setFont(Theme.SIM_STATUS_FONT);

        timerPanel.add(headerL, BorderLayout.NORTH);
        timerPanel.add(timeLabel, BorderLayout.CENTER);

        infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        infoPanel.setBackground(Theme.PANEL_BG);
        infoPanel.add(timerPanel);
        infoPanel.add(statusLabel);
    }

    private void refresh(){
        SimulationEngine.State state = simulationEngine.getState();

        String time = SimulationClock.formatSimTime(simulationEngine.getSimSeconds());

        timeLabel.setText(time);

        switch(state){
            case STOPPED -> {
                statusLabel.setText("Stopped");
                statusLabel.setForeground(Theme.STATUS_STOPPED_COLOR);
                statusLabel.setIcon(Theme.statusDot(Theme.STATUS_STOPPED_COLOR));
                startBtn.setText("Start");
                Theme.setControlButtonEnabled(startBtn, true, Theme.SIM_START_BG);
                Theme.setControlButtonEnabled(pauseBtn, false, Theme.SIM_PAUSE_BG);
                Theme.setControlButtonEnabled(resetBtn, false, Theme.SIM_RESET_BG);
            }
            case RUNNING -> {
                statusLabel.setText("Running");
                statusLabel.setForeground(Theme.STATUS_RUNNING_COLOR);
                statusLabel.setIcon(Theme.statusDot(Theme.STATUS_RUNNING_COLOR));
                startBtn.setText("Start");
                Theme.setControlButtonEnabled(startBtn, false, Theme.SIM_START_BG);
                Theme.setControlButtonEnabled(pauseBtn, true, Theme.SIM_PAUSE_BG);
                Theme.setControlButtonEnabled(resetBtn, true, Theme.SIM_RESET_BG);
            }

            case PAUSED -> {
                statusLabel.setText("Paused");
                statusLabel.setForeground(Theme.STATUS_PAUSED_COLOR);
                statusLabel.setIcon(Theme.statusDot(Theme.STATUS_PAUSED_COLOR));
                startBtn.setText("Resume");
                Theme.setControlButtonEnabled(startBtn, true, Theme.SIM_START_BG);
                Theme.setControlButtonEnabled(pauseBtn, false, Theme.SIM_PAUSE_BG);
                Theme.setControlButtonEnabled(resetBtn, true, Theme.SIM_RESET_BG);
            }
        }
    }

    private void initializeStartBtn(){
        startBtn = new JButton("Start");
        Theme.styleControlButton(startBtn, Theme.SIM_START_BG);
        startBtn.addActionListener(e->{
            if (simulationEngine.getState() != SimulationEngine.State.RUNNING) {
                simulationEngine.start();
                onPause.run();
            }
            refresh();
        });
    }

    private void initializePauseBtn(){
        pauseBtn = new JButton("Pause");
        Theme.styleControlButton(pauseBtn, Theme.SIM_PAUSE_BG);
        pauseBtn.addActionListener(e->{
            if (simulationEngine.getState() == SimulationEngine.State.RUNNING){
                simulationEngine.pause();
                onStart.run();
            }
            refresh();
        });
    }

    private void initializeResetBtn(){
        resetBtn = new JButton("Reset");
        Theme.styleControlButton(resetBtn, Theme.SIM_RESET_BG);
        resetBtn.addActionListener(e->{
            if (simulationEngine.getState() == SimulationEngine.State.RUNNING){
                onStart.run();
            }
            simulationEngine.reset();
            refresh();
        });
    }
}
