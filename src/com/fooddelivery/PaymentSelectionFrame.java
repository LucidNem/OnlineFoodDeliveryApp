package com.fooddelivery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PaymentSelectionFrame extends JFrame {
    private int userId;
    private double totalAmount;

    public PaymentSelectionFrame(int userId, double totalAmount) {
        super("Complete Your Payment");
        this.userId = userId;
        this.totalAmount = totalAmount;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));  // Using GridLayout for simplicity
        panel.add(new JLabel("Total Amount: €" + String.format("%.2f", totalAmount)));

        JButton cardPaymentButton = new JButton("Pay with Card");
        cardPaymentButton.addActionListener(this::processPayment);
        panel.add(cardPaymentButton);

        JButton cashPaymentButton = new JButton("Pay with Cash");
        cashPaymentButton.addActionListener(this::processPayment);
        panel.add(cashPaymentButton);

        add(panel);
        setVisible(true);
    }

    private void processPayment(ActionEvent e) {
        // Assume payment is processed here
        JOptionPane.showMessageDialog(this, "Payment received via " + ((JButton) e.getSource()).getText() + ".\nOrder Completed.", "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();  // Close this window
        System.exit(0);  // Terminate the application
    }
}
