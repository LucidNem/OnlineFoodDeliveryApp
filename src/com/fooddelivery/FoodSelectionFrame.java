package com.fooddelivery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FoodSelectionFrame extends JFrame {
    private int userId;
    private String category;

    public FoodSelectionFrame(int userId, String category) {
        super("Online Food Delivery");
        this.userId = userId;
        this.category = category;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel promptLabel = new JLabel("Choose from our " + category + ":");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(promptLabel, gbc);

        ArrayList<String> foods = loadFoods(category);

        int buttonYIndex = 1;
        for (String foodDetail : foods) {
            JButton foodButton = new JButton(foodDetail);
            foodButton.addActionListener(e -> {
                String[] parts = foodDetail.split(" - €");
                if (parts.length == 2) {
                    try {
                        double itemPrice = Double.parseDouble(parts[1]);
                        CartManager.addItem(parts[0], itemPrice);
                        JOptionPane.showMessageDialog(this, parts[0] + " added to cart!", "Item Added", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Error parsing price for " + parts[1], "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            gbc.gridy = buttonYIndex++;
            panel.add(foodButton, gbc);
        }

        addFunctionalButtons(panel, gbc, buttonYIndex);

        add(panel);
        setVisible(true);
    }

    private ArrayList<String> loadFoods(String category) {
        ArrayList<String> foods = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Adjusted to join with the categories table to access category_name
            String query = "SELECT f.food_name, f.price FROM foods f JOIN categories c ON f.category_id = c.category_id WHERE c.category_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("food_name");
                double price = resultSet.getDouble("price");
                foods.add(name + " - €" + String.format("%.2f", price));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load food items from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return foods;
    }


    private void addFunctionalButtons(JPanel panel, GridBagConstraints gbc, int buttonYIndex) {
        JButton viewCartButton = new JButton("View Cart");
        styleFunctionalButton(viewCartButton);
        viewCartButton.addActionListener(e -> JOptionPane.showMessageDialog(this, CartManager.getCartDetails(), "Cart", JOptionPane.INFORMATION_MESSAGE));
        gbc.gridy = buttonYIndex++;
        panel.add(viewCartButton, gbc);

        JButton completeOrderButton = new JButton("Complete Order");
        styleFunctionalButton(completeOrderButton);
        completeOrderButton.addActionListener(e -> {
            if (!CartManager.isCartEmpty()) {
                new PaymentSelectionFrame(userId, CartManager.getTotalAmount());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Your cart is empty. Please add items before completing the order.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            }
        });
        gbc.gridy = buttonYIndex++;
        panel.add(completeOrderButton, gbc);

        JButton backButton = new JButton("Back to Categories");
        styleFunctionalButton(backButton);
        backButton.addActionListener(e -> {
            new CategorySelectionFrame(userId);
            dispose();
        });
        gbc.gridy = buttonYIndex;
        panel.add(backButton, gbc);
    }

    private void styleFunctionalButton(JButton button) {
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
}
