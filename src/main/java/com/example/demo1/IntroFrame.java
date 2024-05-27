package com.example.demo1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroFrame extends JFrame {
    public IntroFrame() {
        setTitle("Bienvenida a Batalla Naval");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Crear el panel de contenido
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(10, 25, 50)); // Fondo oscuro para contraste
        add(panel);

        // Título
        JLabel titleLabel = new JLabel("Naval Battle");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(new Color(0, 169, 255)); // Color de texto con buen contraste
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 50))); // Espacio en blanco
        panel.add(titleLabel);

        // Instrucciones
        JTextArea instructionsArea = new JTextArea(10, 40);
        instructionsArea.setText("Instrucciones básicas:\n"
                + "1. Coloca tus barcos en la cuadrícula blanca.\n"
                + "2. Haz doble clic en la cuadrícula gris para lanzar bombas.\n"
                + "3. El objetivo es hundir todos los barcos enemigos.\n"
                + "4. Los barcos pueden rotarse presionando la tecla 'R'.");
        instructionsArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        instructionsArea.setForeground(Color.WHITE);
        instructionsArea.setBackground(new Color(10, 25, 50)); // Fondo igual al panel
        instructionsArea.setEditable(false);
        instructionsArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Borde blanco
        panel.add(Box.createRigidArea(new Dimension(0, 50))); // Espacio en blanco
        panel.add(instructionsArea);

        // Botón de jugar
        JButton playButton = new JButton("Jugar");
        playButton.setFont(new Font("SansSerif", Font.BOLD, 36));
        playButton.setBackground(new Color(0, 169, 255)); // Fondo del botón
        playButton.setForeground(Color.WHITE); // Texto del botón
        playButton.setFocusPainted(false); // Quitar el borde de foco
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cerrar la ventana actual
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Batalla Naval");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(1200, 600); // Ajustar el tamaño de la ventana
                    frame.add(new MainPanel());
                    frame.setVisible(true);
                });
            }
        });
        panel.add(Box.createRigidArea(new Dimension(0, 50))); // Espacio en blanco
        panel.add(playButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IntroFrame introFrame = new IntroFrame();
            introFrame.setVisible(true);
        });
    }
}
