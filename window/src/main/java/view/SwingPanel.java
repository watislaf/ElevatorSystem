package view;

import javax.swing.*;
import java.awt.*;

class SwingPanel extends JPanel {
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawHelpCercle(g2d);
    }

    private void drawHelpCercle(Graphics2D g2d) {
        g2d.setColor(Color.gray.darker().darker());
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        String characters_to_use = "FCXSER";
        Point help_center = new Point(500, 500);
        help_center.x -= 12;
        help_center.y += 12;
        for (int x = 0; x < characters_to_use.length(); x++) {
            Double c = Math.cos(x * 60 * Math.PI / 180.) * (120);
            Double s = Math.sin(x * 60 * Math.PI / 180.) * (120);
            g2d.drawString("" + characters_to_use.charAt(x), help_center.x + c.intValue(), help_center.y + s.intValue());
        }

        g2d.drawString("D", help_center.x, help_center.y);
    }
}