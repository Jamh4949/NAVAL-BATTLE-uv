package com.example.demo1;
import javax.swing.JPanel;
import java.awt.*;

public class MainPanel extends JPanel {
    private ShipPanel whiteGrid;
    private ShipPanel grayGrid;

    public MainPanel() {
        setLayout(new GridLayout(1, 2));

        // Crear los dos grid panes
        whiteGrid = new ShipPanel(Color.WHITE);
        grayGrid = new ShipPanel(Color.GRAY);

        // Añadir los grids al panel principal
        add(whiteGrid);
        add(grayGrid);

        // Establecer el panel blanco en el panel gris para referencia cruzada
        grayGrid.setEnemyPanel(whiteGrid);
    }
}
