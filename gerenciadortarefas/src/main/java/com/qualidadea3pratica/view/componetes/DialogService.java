package com.qualidadea3pratica.view.componetes;

import java.awt.Component;
import javax.swing.Icon;

public interface DialogService {
    void showMessage(Component parentComponent, Object message, String title, int messageType);
    int showConfirmDialog(Component parentComponent, Object message, String title, int optionType);
    Object showInputDialog(Component parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue);
}