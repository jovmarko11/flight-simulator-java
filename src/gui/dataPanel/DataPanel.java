package gui.dataPanel;

import gui.Theme;

import javax.swing.*;
import java.awt.*;

public abstract class DataPanel extends JPanel {
    protected JPanel formPanel;
    protected JPanel tablePanel;
    protected JPanel buttonPanel;

    protected JButton submitBtn;
    protected JButton deleteBtn;

    protected JTable table;
    protected JScrollPane scrollPane;

    public DataPanel(){
        setLayout(new BorderLayout());
    }

    protected void initializeComponents(){
        initializeFormPanel();
        initializeTablePanel();
        initializeButtonPanel();

        this.add(formPanel,BorderLayout.NORTH);
        this.add(tablePanel,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.SOUTH);
    }

    private void initializeButtonPanel(){
        buttonPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        buttonPanel.setBackground(Theme.PANEL_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, Theme.PANEL_PADDING, 8, Theme.PANEL_PADDING));

        submitBtn = new JButton("Add");
        Theme.styleButton(submitBtn, Theme.PRIMARY_BTN_BG);
        deleteBtn = new JButton("Remove");
        Theme.styleButton(deleteBtn, Theme.ALERT_RED);

        initializeSubmitBtn();
        initializeDeleteBtn();

        buttonPanel.add(deleteBtn);
        buttonPanel.add(submitBtn);
    }

    private void initializeTablePanel(){
        table = initializeTable();
        Theme.styleTable(table);

        scrollPane = new JScrollPane(table);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Theme.PANEL_BG);
        tablePanel.add(scrollPane,BorderLayout.CENTER);
    }


    // Implemented by the concrete panels (airports, flights)
    protected abstract void initializeSubmitBtn();
    protected abstract void initializeDeleteBtn();
    protected abstract void initializeFormPanel();
    protected abstract JTable initializeTable();

}
