package view;

import lombok.RequiredArgsConstructor;
import model.ViewModel;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
class SwingPanel extends JPanel {
    private final ViewModel VIEW_MODEL;
    ;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (VIEW_MODEL.getSettings() == null) {
            return;
        }
        g2d.translate(0, VIEW_MODEL.getSettings().BUILDING_SIZE.y);
        drawBuilding(g2d);
        VIEW_MODEL.getDrawable().forEach(drawable -> drawable.Draw(g));
    }

    private void drawBuilding(Graphics2D g2d) {
        g2d.setColor(Color.white);
        // 100 , 300
        g2d.drawRect(0, -VIEW_MODEL.getSettings().BUILDING_SIZE.y,
                VIEW_MODEL.getSettings().BUILDING_SIZE.x,
                VIEW_MODEL.getSettings().BUILDING_SIZE.y);
    }
}