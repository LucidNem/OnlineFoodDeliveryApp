package com.fooddelivery;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame();  // Initialize and show the LoginFrame
            }
        });
    }
}
