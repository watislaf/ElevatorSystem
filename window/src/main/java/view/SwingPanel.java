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
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        if (!VIEW_MODEL.isInitialised()) {
            return;
        }
        var gameSize = VIEW_MODEL.getSettings().BUILDING_SIZE;
        var realSize = this.getSize();
        var blackZone = new Point(100, 100);

        g2d.translate(blackZone.x / 2, blackZone.y / 2);
        GameDrawer gameDrawer = new GameDrawer(gameSize, new Point(
                realSize.width - blackZone.x,
                realSize.height - blackZone.y), g2d);

        drawWall(gameDrawer);
        VIEW_MODEL.getDrawableOjects().forEach(drawable -> drawable.draw(gameDrawer));
        drawBuilding(gameDrawer);
    }

    private void drawBuilding(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(VIEW_MODEL.getColorSettings().BETON_COLOR);
            gameDrawer.drawRect(
                    new Vector2D(VIEW_MODEL.getSettings().BUILDING_SIZE.x / 2., i * floorHeight),
                    new Point(VIEW_MODEL.getSettings().BUILDING_SIZE.x, floorHeight), 7);

            gameDrawer.setColor(VIEW_MODEL.getColorSettings().BLACK_SPACE_COLOR);
            gameDrawer.fillRect(
                    new Vector2D(0 - VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, i * floorHeight),
                    new Point(VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, floorHeight)
            );
            gameDrawer.fillRect(
                    new Vector2D(
                            VIEW_MODEL.getSettings().BUILDING_SIZE.x, i * floorHeight),
                    new Point(VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, floorHeight)
            );
        }

    }

    private void drawWall(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(VIEW_MODEL.getColorSettings().WALL_COLOR);
            gameDrawer.fillRect(
                    new Vector2D(0, i * floorHeight),
                    new Point(VIEW_MODEL.getSettings().BUILDING_SIZE.x, floorHeight));
        }
    }
}