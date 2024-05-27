package com.example.demo1;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipPanel extends JPanel {

    private static final int GRID_SIZE = 50;
    private static final int GRID_ROWS = 10;
    private static final int GRID_COLS = 10;

    private List<ShipDrawing> ships;
    private ShipDrawing selectedShip;
    private int offsetX, offsetY;
    private Color backgroundColor;
    private List<Bomb> bombs;
    private List<ShipDrawing> enemyShips;
    private ShipPanel enemyPanel; // Añadido para referenciar el panel enemigo

    public ShipPanel(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        ships = new ArrayList<>();
        bombs = new ArrayList<>();
        enemyShips = new ArrayList<>();
        if (backgroundColor == Color.WHITE) {
            // Crear y añadir los barcos solo en el panel blanco
            ships.add(new ShipDrawing(50, 50, 200, 50, "Portaaviones"));
            ships.add(new ShipDrawing(50, 150, 150, 50, "Submarino"));
            ships.add(new ShipDrawing(250, 150, 150, 50, "Submarino"));
            ships.add(new ShipDrawing(50, 250, 100, 50, "Destructor"));
            ships.add(new ShipDrawing(200, 250, 100, 50, "Destructor"));
            ships.add(new ShipDrawing(350, 250, 100, 50, "Destructor"));
            ships.add(new ShipDrawing(50, 350, 50, 50, "Fragata"));
            ships.add(new ShipDrawing(150, 350, 50, 50, "Fragata"));
            ships.add(new ShipDrawing(250, 350, 50, 50, "Fragata"));
            ships.add(new ShipDrawing(350, 350, 50, 50, "Fragata"));
        } else if (backgroundColor == Color.GRAY) {
            // Crear y añadir los barcos enemigos en el panel gris
            generateRandomEnemyShips();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (backgroundColor == Color.WHITE) {
                    for (ShipDrawing ship : ships) {
                        if (ship.contains(e.getX(), e.getY())) {
                            selectedShip = ship;
                            offsetX = e.getX() - ship.getX();
                            offsetY = e.getY() - ship.getY();
                            break;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedShip != null) {
                    snapToGrid(selectedShip);
                    selectedShip = null;
                    repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && backgroundColor == Color.GRAY) {
                    int gridX = e.getX() / GRID_SIZE;
                    int gridY = e.getY() / GRID_SIZE;
                    if (gridX >= 0 && gridX < GRID_COLS && gridY >= 0 && gridY < GRID_ROWS) {
                        Bomb bomb = new Bomb(gridX * GRID_SIZE, gridY * GRID_SIZE);
                        bombs.add(bomb);
                        checkBombHit(bomb);
                        if (enemyPanel != null) {
                            enemyPanel.performEnemyAttack();
                        }
                        repaint();
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedShip != null) {
                    selectedShip.setPosition(e.getX() - offsetX, e.getY() - offsetY);
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (selectedShip != null) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        selectedShip.rotate();
                        snapToGrid(selectedShip);
                        repaint();
                    }
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Pintar el fondo
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Dibujar las líneas de la cuadrícula
        g2d.setColor(Color.BLACK);
        for (int i = 0; i <= GRID_ROWS; i++) {
            g2d.drawLine(0, i * GRID_SIZE, GRID_COLS * GRID_SIZE, i * GRID_SIZE);
        }
        for (int i = 0; i <= GRID_COLS; i++) {
            g2d.drawLine(i * GRID_SIZE, 0, i * GRID_SIZE, GRID_ROWS * GRID_SIZE);
        }

        // Dibujar los barcos
        for (ShipDrawing ship : ships) {
            ship.dibujar(g2d);
        }

        // Dibujar las bombas
        for (Bomb bomb : bombs) {
            bomb.dibujar(g2d);
        }
    }

    // Cambia el método snapToGrid
    private void snapToGrid(ShipDrawing ship) {
        int newX = ((ship.getX() + GRID_SIZE / 2) / GRID_SIZE) * GRID_SIZE;
        int newY = ((ship.getY() + GRID_SIZE / 2) / GRID_SIZE) * GRID_SIZE;

        // Verificar que el barco no esté fuera del panel
        if (newX < 0) newX = 0;
        if (newY < 0) newY = 0;
        if (newX + ship.getWidth() > GRID_COLS * GRID_SIZE) {
            newX = GRID_COLS * GRID_SIZE - ship.getWidth();
        }
        if (newY + ship.getHeight() > GRID_ROWS * GRID_SIZE) {
            newY = GRID_ROWS * GRID_SIZE - ship.getHeight();
        }

        ship.setPosition(newX, newY);
    }

    private void generateRandomEnemyShips() {
        Random rand = new Random();

        // Generar barcos enemigos invisibles
        addRandomEnemyShip(rand, 4); // Portaaviones
        addRandomEnemyShip(rand, 3); // Submarino 1
        addRandomEnemyShip(rand, 3); // Submarino 2
        addRandomEnemyShip(rand, 2); // Destructor 1
        addRandomEnemyShip(rand, 2); // Destructor 2
        addRandomEnemyShip(rand, 2); // Destructor 3
        addRandomEnemyShip(rand, 1); // Fragata 1
        addRandomEnemyShip(rand, 1); // Fragata 2
        addRandomEnemyShip(rand, 1); // Fragata 3
        addRandomEnemyShip(rand, 1); // Fragata 4
    }

    private void addRandomEnemyShip(Random rand, int size) {
        while (true) {
            int x = rand.nextInt(GRID_COLS - size + 1) * GRID_SIZE;
            int y = rand.nextInt(GRID_ROWS) * GRID_SIZE;
            ShipDrawing ship = new ShipDrawing(x, y, size * GRID_SIZE, GRID_SIZE, "Enemy");
            if (isPositionValid(ship)) {
                enemyShips.add(ship);
                break;
            }
        }
    }

    private boolean isPositionValid(ShipDrawing newShip) {
        for (ShipDrawing ship : enemyShips) {
            if (ship.intersects(newShip)) {
                return false;
            }
        }
        return true;
    }

    private void checkBombHit(Bomb bomb) {
        for (ShipDrawing ship : enemyShips) {
            if (ship.contains(bomb.getX(), bomb.getY())) {
                bomb.markAsHit();
                break;
            }
        }
    }

    public void performEnemyAttack() {
        Random rand = new Random();
        int gridX = rand.nextInt(GRID_COLS);
        int gridY = rand.nextInt(GRID_ROWS);

        Bomb enemyBomb = new Bomb(gridX * GRID_SIZE, gridY * GRID_SIZE);

        // Verifica si el ataque enemigo golpea un barco aliado
        boolean hit = false;
        for (ShipDrawing ship : ships) {
            if (ship.contains(enemyBomb.getX(), enemyBomb.getY())) {
                enemyBomb.markAsHit();
                hit = true;
                break;
            }
        }

        bombs.add(enemyBomb);
        repaint();
    }

    public void setEnemyPanel(ShipPanel enemyPanel) {
        this.enemyPanel = enemyPanel;
    }

    class Bomb {
        private int x, y;
        private boolean hit;

        public Bomb(int x, int y) {
            this.x = x;
            this.y = y;
            this.hit = false;
        }

        public void dibujar(Graphics2D g2d) {
            if (hit) {
                g2d.setColor(Color.BLACK);
            } else {
                g2d.setColor(Color.RED);
            }
            g2d.fillRect(x, y, GRID_SIZE, GRID_SIZE);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void markAsHit() {
            this.hit = true;
        }
    }
}
