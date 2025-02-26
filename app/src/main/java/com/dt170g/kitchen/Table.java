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

    public String getTableNumber() { return tableNumber; }
    public List<Order> getOrders() { return orders; }

    public void addOrder(Order order) { orders.add(order); }
    public void markOrderReady(String dish) {
        for (Order order : orders) {
            if (order.getDish().equalsIgnoreCase(dish)) {
                // Mark only the correct dish as ready
                if (dish.equalsIgnoreCase("Förrätt")) {
                    order.markStarterReady();
                } else if (dish.equalsIgnoreCase("Huvudrätt")) {
                    order.markMainCourseReady();
                } else if (dish.equalsIgnoreCase("Efterrätt")) {
                    order.markDessertReady();
                }
                break; // Exit after marking the first matching dish
            }
        }
    }

}
