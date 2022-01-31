
package view;


import controller.WindowController;
import model.WindowModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

public class SwingWindow {
    private ActionListener listener;
    private WindowController controller;
    private SwingPanel startPanel;
    final Point WINDOW_SIZE = new Point(800, 800);
    private final LinkedList<JButton> BUTTONS = new LinkedList<>();

    private JFrame frame;
    private Dimension resize;

    public void startWindow(WindowModel windowModel, WindowController controller) {
        this.controller = controller;
        initializeWindow(windowModel);
        initializeButtons(windowModel);
    }

    private void initializeButtons(WindowModel windowModel) {
        listener = new GuiActionListener(this);
        for (int i = 0; i < 16; i++) {
            var start_button_ = new JButton("2");
            start_button_.setSize(55, 55);
            start_button_.addActionListener(listener);
            start_button_.setBackground(windowModel.COLOR_SETTINGS.JBUTTONS_COLOR);
            start_button_.setFocusPainted(false);
            start_button_.setForeground(Color.white);
            start_button_.setVisible(false);
            start_button_.setEnabled(false);


            startPanel.add(start_button_);
            BUTTONS.add(start_button_);
        }
    }

    private void initializeWindow(WindowModel windowModel) {
        startPanel = new SwingPanel(windowModel);
        startPanel.setBackground(windowModel.COLOR_SETTINGS.BLACK_SPACE_COLOR);
        startPanel.setLayout(null);

        frame = new JFrame("ELEVATOR SYS");
        frame.setSize(WINDOW_SIZE.x, WINDOW_SIZE.y);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(startPanel);
    }


    public void repaint() {
        frame.repaint();
    }

    public void updateButtonsAndSliders(WindowModel windowMODEL) {
        resize = startPanel.getSize();
        Iterator<JButton> button = BUTTONS.iterator();
        double heightOfButton = (resize.height - 100.) / windowMODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < 16; i++) {
            JButton currentButton = button.next();
            currentButton.setText("->");
            if (i >= windowMODEL.getSettings().FLOORS_COUNT) {
                currentButton.setVisible(false);
                continue;
            }
            currentButton.setVisible(true);
            currentButton.setEnabled(true);
            currentButton.setBounds(
                    new Rectangle(0, (int) heightOfButton * i + 50,
                            50, (int) heightOfButton));

        }

    }

    public boolean resized() {
        if (resize == null) {
            return false;
        }
        return resize == startPanel.getSize();
    }

    public void clicked(JButton source) {
        Iterator<JButton> button = BUTTONS.iterator();
        for (int i = 0; i < 16; i++) {
            JButton currentButton = button.next();
            if (currentButton == source) {
                controller.clickedButtonWithNumber(i);
                return;
            }
        }
    }
}

