package view;

import lombok.RequiredArgsConstructor;
import model.WindowModel;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
class SwingPanel extends JPanel {
    private final WindowModel VIEW_MODEL;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        if (VIEW_MODEL.getSettings() == null) {
            return;
        }
        var gameSize = VIEW_MODEL.getSettings().BUILDING_SIZE;
        var realSize = this.getSize();
        var blackZone = new Point(100, 100);

        g2d.translate(blackZone.x / 2, blackZone.y / 2);
        GameDrawer gameDrawer = new GameDrawer(gameSize, new Point(
                realSize.width - blackZone.x,
                realSize.height - blackZone.y), g2d);

        drawBackGround(gameDrawer);
        VIEW_MODEL.getDrawableOjects().forEach(drawable -> drawable.draw(gameDrawer));
        drawBuilding(gameDrawer);
    }

    private void drawBuilding(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        gameDrawer.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.drawRect(
                    new Vector2D(VIEW_MODEL.getSettings().BUILDING_SIZE.x / 2., i * floorHeight),
                    new Point(VIEW_MODEL.getSettings().BUILDING_SIZE.x, floorHeight), 2);

        }
    }

    private void drawBackGround(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(Color.GRAY.darker());
            gameDrawer.fillRect(
                    new Vector2D(0, i * floorHeight),
                    new Point(VIEW_MODEL.getSettings().BUILDING_SIZE.x, floorHeight));
        }
    }
}