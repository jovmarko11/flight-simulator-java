package gui;

import airtraffic.service.DataService;
import gui.utils.ErrorDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileMenuBar extends JMenuBar {
    private final DataService dataService;
    private JMenu fileMenu;

    private JMenuItem csvImport;
    private JMenuItem csvExport;
    private JMenuItem jsonImport;
    private JMenuItem jsonExport;

    private enum Handler {
        CSV("csv"), JSON("json");
        final String suffix;
        Handler(String suffix) {
            this.suffix = suffix;
        }
    }

    public FileMenuBar(DataService dataService) {
        this.dataService = dataService;

        initializeMenu();
        addActionListeners();


        this.add(fileMenu);
    }

    private void initializeMenu(){
        fileMenu = new JMenu("File");
        fileMenu.setFont(Theme.UI_FONT);
        fileMenu.setBackground(Theme.TABLE_HEADER_BG);
        fileMenu.setForeground(Theme.TABLE_HEADER_FG);

        csvImport = new JMenuItem("Import CSV");
        jsonImport = new JMenuItem("Import JSON");
        csvExport = new JMenuItem("Export CSV");
        jsonExport = new JMenuItem("Export JSON");

        csvImport.setFont(Theme.UI_FONT);
        csvExport.setFont(Theme.UI_FONT);
        jsonImport.setFont(Theme.UI_FONT);
        jsonExport.setFont(Theme.UI_FONT);

        fileMenu.add(csvImport);
        fileMenu.add(csvExport);
        fileMenu.addSeparator();
        fileMenu.add(jsonImport);
        fileMenu.add(jsonExport);
    }

    private void addActionListeners(){
        csvImport.addActionListener(e -> importData(Handler.CSV));
        jsonImport.addActionListener(e -> importData(Handler.JSON));
        csvExport.addActionListener(e -> exportData(Handler.CSV));
        jsonExport.addActionListener(e -> exportData(Handler.JSON));
    }

    private void importData(Handler h){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(h.suffix.toUpperCase() + " File", h.suffix));
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;
        try{
            switch (h){
                case CSV: dataService.importFromCsv(chooser.getSelectedFile()); break;
                case JSON: dataService.importFromJson(chooser.getSelectedFile()); break;
            }
        }
        catch (Exception ex){
            ErrorDialog.showError(this, ex.getMessage());
        }
    }

    private void exportData(Handler h) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(h.suffix.toUpperCase() + " File", h.suffix));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith("." + h.suffix)) {
            file = new File(file.getParentFile(), file.getName() + "." + h.suffix);
        }

        try {
            switch (h){
                case CSV: dataService.exportToCsv(file); break;
                case JSON: dataService.exportToJson(file); break;
            }
        } catch (Exception ex) {
            ErrorDialog.showError(this, ex.getMessage());
        }
    }
}
