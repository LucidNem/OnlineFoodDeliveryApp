package com.fooddelivery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CategorySelectionFrame extends JFrame {
    private int userId;

    public CategorySelectionFrame(int userId) {
        this.userId = userId;

        setTitle("Online food delivery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel promptLabel = new JLabel("Please select a food category:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(promptLabel, gbc);

        ArrayList<String> categories = loadCategories();

        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                JButton categoryButton = new JButton(categories.get(i));
                categoryButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new FoodSelectionFrame(userId, categoryButton.getText());
                        dispose(); // Close the current frame
                    }
                });
                gbc.gridy = i + 1; // Start buttons after the prompt
                panel.add(categoryButton, gbc);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load categories from the database", "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(panel);
        setVisible(true);
    }

    private ArrayList<String> loadCategories() {
        ArrayList<String> categories = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT category_name FROM Categories";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                categories.add(resultSet.getString("category_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            categories = null;
        }
        return categories;
    }
}
