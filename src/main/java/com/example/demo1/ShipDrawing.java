package com.example.demo1;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ShipDrawing {
    private int x, y, width, height;
    private String type;
    private Color color;
    private double angle;
    private List<Boolean> damage;
    private boolean isVertical;

    public ShipDrawing(int x, int y, int width, int height, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.color = determineColor(type);
        this.angle = 0;
        this.damage = new ArrayList<>();
        this.isVertical = false;

        for (int i = 0; i < (width * height) / (50 * 50); i++) {
            this.damage.add(false);
        }
    }

    private Color determineColor(String type) {
        switch (type) {
            case "Portaaviones":
                return Color.GRAY;
            case "Submarino":
                return Color.YELLOW;
            case "Destructor":
                return Color.GREEN;
            case "Fragata":
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }

    public void dibujar(Graphics2D g2d) {
        AffineTransform old = g2d.getTransform();
        g2d.translate(x + width / 2, y + height / 2);
        g2d.rotate(Math.toRadians(angle));
        g2d.translate(-width / 2, -height / 2);

        // Draw the ship body
        g2d.setColor(color);
        g2d.fillRoundRect(0, 0, width, height, 15, 15);

        // Draw ship details
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, width, height, 15, 15);

        if ("Portaaviones".equals(type) || "Submarino".equals(type)) {
            g2d.fillRect(width / 2 - 10, -10, 20, 10);
        }

        if ("Submarino".equals(type)) {
            g2d.fillOval(width - 20, height / 2 - 5, 10, 10);
        }

        if ("Destructor".equals(type) || "Fragata".equals(type)) {
            for (int i = 0; i < width / 20; i++) {
                g2d.fillOval(10 + i * 20, height / 4, 10, 10);
            }
        }

        for (int i = 0; i < damage.size(); i++) {
            if (damage.get(i)) {
                int damageX = (i % (width / 50)) * 50;
                int damageY = (i / (width / 50)) * 50;
                g2d.setColor(Color.RED);
                g2d.fillRect(damageX, damageY, 50, 50);
            }
        }

        g2d.setTransform(old);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void rotate() {
        int temp = width;
        width = height;
        height = temp;
        isVertical = !isVertical;
    }

    public boolean contains(int mouseX, int mouseY) {
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), x + width / 2, y + height / 2);
        Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
        return at.createTransformedShape(rect).contains(mouseX, mouseY);
    }

    public boolean intersects(ShipDrawing other) {
        Rectangle2D thisRect = new Rectangle2D.Float(x, y, width, height);
        Rectangle2D otherRect = new Rectangle2D.Float(other.x, other.y, other.width, other.height);
        return thisRect.intersects(otherRect);
    }

    public boolean hit(int bombX, int bombY) {
        if (contains(bombX, bombY)) {
            int relativeX = bombX - x;
            int relativeY = bombY - y;
            int index = (relativeY / 50) * (width / 50) + (relativeX / 50);
            if (index >= 0 && index < damage.size()) {
                damage.set(index, true);
            }
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSunk() {
        for (boolean part : damage) {
            if (!part) {
                return false;
            }
        }
        return true;
    }
}
