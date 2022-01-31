package view;

import lombok.RequiredArgsConstructor;

import java.awt.event.ActionListener;
import javax.swing.*;


import java.awt.event.ActionEvent;

@RequiredArgsConstructor
public class GuiActionListener implements ActionListener {
    private final SwingWindow WINDOW;

    @Override
    public void actionPerformed(ActionEvent e) {
        WINDOW.clicked((JButton) e.getSource());
        WINDOW.repaint();
    }
}
