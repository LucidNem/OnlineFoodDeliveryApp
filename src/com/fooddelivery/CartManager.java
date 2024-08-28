package com.fooddelivery;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final List<String> cartItems = new ArrayList<>();
    private static double totalAmount = 0.0;

    public static void addItem(String item, double price) {
        cartItems.add(item + " - €" + String.format("%.2f", price));
        totalAmount += price;
        System.out.println("Added to Cart: " + item + " | Total Now: €" + String.format("%.2f", totalAmount));
    }

    public static String getCartDetails() {
        if (cartItems.isEmpty()) {
            return "Your cart is empty.";
        } else {
            StringBuilder details = new StringBuilder("Your cart contains:\n");
            for (String item : cartItems) {
                details.append(item).append("\n");
            }
            details.append("Total Amount: €").append(String.format("%.2f", totalAmount));
            return details.toString();
        }
    }

    public static boolean isCartEmpty() {
        return cartItems.isEmpty();
    }

    public static double getTotalAmount() {
        return totalAmount;
    }

    public static void clearCart() {
        cartItems.clear();
        totalAmount = 0.0;
    }
}
