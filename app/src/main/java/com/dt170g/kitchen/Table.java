package com.dt170g.kitchen;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String tableNumber;
    private List<Order> orders;

    public Table(String tableNumber) {
        this.tableNumber = tableNumber;
        this.orders = new ArrayList<>();
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Marks the first matching dish (by name/type) in the orders as ready.
     * For example, dishType can be "Förrätt", "Huvudrätt", or "Efterrätt".
     */
    public void markOrderDishReady(String dishType) {
        for (Order order : orders) {
            if (order.getOrderDishes() != null) {
                for (OrderDish orderDish : order.getOrderDishes()) {
                    // Check if the Dish object exists and its name matches the dishType
                    if (orderDish.getDish() != null &&
                            orderDish.getDish().getName().equalsIgnoreCase(dishType)) {
                        // Mark this dish as ready by setting its status
                        orderDish.setStatus("READY");
                        // Optionally, you might want to update the order status as well
                        return; // Exit after marking the first matching dish
                    }
                }
            }
        }
    }
}
