
package view;


import model.WindowModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SwingWindow {
    private ActionListener listener;
    private JButton start_button_;
    private SwingPanel start_panel_;
    final Point WINDOW_SIZE_ = new Point(800, 800);

    private JFrame frame_;
    private Timer main_timer_;


    public void startWindow(WindowModel windowModel) {
        initializeWindow(windowModel);
        initializeButtons();
        initializeTimer(60);
    }

    private void initializeButtons() {
        start_button_ = new JButton("Start");
        int buttonWidth = 150;
        int buttonHeight = 100;
        start_button_.setSize(new Dimension(buttonWidth, buttonHeight));
        start_button_.setFocusable(false);
        start_button_.addActionListener(listener);
        start_button_.setBackground(Color.black);
        start_button_.setFocusPainted(false);
        start_button_.setForeground(Color.black);
        start_button_.setVisible(true);
        start_panel_.add(start_button_);
    }

    private void initializeTimer(int interval) {
        main_timer_ = new Timer(interval, listener);
        main_timer_.start();
    }

    private void initializeWindow(WindowModel windowModel) {
        start_panel_ = new SwingPanel(windowModel);
        start_panel_.setBackground(Color.BLACK);
        start_panel_.setLayout(null);

        frame_ = new JFrame("ELEVATOR SYS");
        frame_.setSize(WINDOW_SIZE_.x, WINDOW_SIZE_.y);
        frame_.setVisible(true);
        frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_.add(start_panel_);
    }


    public void repaint() {
        //      WINDOW_SIZE_.y = frame_.getSize().height;
        //      double y_position = (400);
        //      start_button_.setBounds(new Rectangle((WINDOW_SIZE_.x - 14) / 2, (int) y_position, 555, 555));
        //      WINDOW_SIZE_.x = frame_.getSize().width;
        frame_.repaint();
    }
}

