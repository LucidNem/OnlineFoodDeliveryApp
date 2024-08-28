package com.fooddelivery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderConfirmationFrame extends JFrame {

    private JTextArea orderSummaryTextArea;
    private JButton confirmOrderButton;
    private List<String> cartItems;
    private String paymentMethod;

    public OrderConfirmationFrame(List<String> cartItems, String paymentMethod) {
        this.cartItems = cartItems;
        this.paymentMethod = paymentMethod;
        setTitle("Online food delivery");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        orderSummaryTextArea = new JTextArea();
        orderSummaryTextArea.setEditable(false);
        confirmOrderButton = new JButton("Confirm Order");

        // Layout setup
        setLayout(new BorderLayout());
        add(new JScrollPane(orderSummaryTextArea), BorderLayout.CENTER);
        add(confirmOrderButton, BorderLayout.SOUTH);

        // Display order summary
        displayOrderSummary();

        // Add action listener for the confirm button
        confirmOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmOrder();
            }
        });
    }

    private void displayOrderSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Order Summary:\n");
        for (String item : cartItems) {
            summary.append(item).append("\n");
        }
        summary.append("\nPayment Method: ").append(paymentMethod).append("\n");
        orderSummaryTextArea.setText(summary.toString());
    }

    private void confirmOrder() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String orderQuery = "INSERT INTO Orders (order_details, payment_method) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(orderQuery);
            StringBuilder orderDetails = new StringBuilder();
            for (String item : cartItems) {
                orderDetails.append(item).append(", ");
            }
            ps.setString(1, orderDetails.toString());
            ps.setString(2, paymentMethod);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order confirmed!", "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
            dispose();  // Close the window after order is confirmed
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to confirm order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
