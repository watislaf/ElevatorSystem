package view;

import javax.swing.*;
import java.awt.event.ActionListener;


import java.awt.event.ActionEvent;

public class GuiActionListener implements ActionListener {
    SwingWindow window;

    public GuiActionListener(SwingWindow window) {
        this.window = window;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        window.clicked((JButton) e.getSource());
        window.repaint();
    }
}
