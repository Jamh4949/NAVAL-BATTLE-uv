package com.example.demo1;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IntroFrame introFrame = new IntroFrame();
            introFrame.setVisible(true);
        });
    }
}


