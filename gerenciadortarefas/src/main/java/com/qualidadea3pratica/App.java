package com.qualidadea3pratica;

import javax.swing.SwingUtilities;

import com.qualidadea3pratica.view.MainFrame;

public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
