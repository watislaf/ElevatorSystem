
package view;


import controller.WindowController;
import drawable.ColorSettings;
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
    private final LinkedList<JButton> ADD_CLIENT_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> ADD_REDUCE_ELEVATORS_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> CHANGE_SPEED_BUTTONS = new LinkedList<>();

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
            var buttonCreated = createButton(
                    "->", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR);
            buttonCreated.setVisible(false);
            ADD_CLIENT_BUTTONS.add(buttonCreated);
        }
        ADD_REDUCE_ELEVATORS_BUTTONS.add(createButton("^", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR));
        ADD_REDUCE_ELEVATORS_BUTTONS.add(createButton("v", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR));
        CHANGE_SPEED_BUTTONS.add(createButton("<", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR));
        CHANGE_SPEED_BUTTONS.add(createButton(">", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR));
    }

    private JButton createButton(String text, Color buttonColor) {
        var startButton = new JButton(text);
        startButton.addActionListener(listener);
        startButton.setBackground(buttonColor);
        startButton.setForeground(Color.white);
        startButton.setFocusPainted(false);
        startPanel.add(startButton);
        return startButton;
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
        Iterator<JButton> button = ADD_CLIENT_BUTTONS.iterator();
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
        for (int i = 0; i < 2; i++) {
            ADD_REDUCE_ELEVATORS_BUTTONS.get(i).setBounds(
                    new Rectangle(50, 50 + i * 100,
                            50, 50));
            CHANGE_SPEED_BUTTONS.get(i).setBounds(
                    new Rectangle(50 + i * 50, 100,
                            50, 50));
        }

    }

    public boolean resized() {
        if (resize == null) {
            return false;
        }
        return resize == startPanel.getSize();
    }

    public void clicked(JButton source) {
        Iterator<JButton> button = ADD_CLIENT_BUTTONS.iterator();
        for (int i = 0; i < 16; i++) {
            JButton currentButton = button.next();
            if (currentButton == source) {
                controller.clickedAddCustomerButtonWithNumber(i);
                return;
            }
        }
        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(0) == source) {
            controller.changeElevatorsCount(true);
        }
        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(1) == source) {
            controller.changeElevatorsCount(false);
        }
        if (CHANGE_SPEED_BUTTONS.get(0) == source) {
            controller.decreesSpeed();
        }
        if (CHANGE_SPEED_BUTTONS.get(1) == source) {
            controller.increaseSpeed();
        }
    }
}

