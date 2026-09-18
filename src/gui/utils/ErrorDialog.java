package gui.utils;

import javax.swing.*;
import java.awt.*;

public class ErrorDialog {
    private ErrorDialog() {}

    public static void showError(Component parent, String message){
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message){
        JOptionPane.showMessageDialog(parent, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

}
