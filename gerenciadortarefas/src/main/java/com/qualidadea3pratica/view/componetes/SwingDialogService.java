package com.qualidadea3pratica.view.componetes;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class SwingDialogService implements DialogService {
    @Override
    public void showMessage(Component parentComponent, Object message, String title, int messageType) {
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
    }

    @Override
    public int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType);
    }

    @Override
    public Object showInputDialog(Component parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) {
        return JOptionPane.showInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
    }
}