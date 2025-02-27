package com.dt170g.kitchen;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static OrderManager instance;
    private List<Table> tables;

    private OrderManager() {
        tables = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            tables.add(new Table("Bord " + i));
        }
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void addOrder(String tableNumber, String dish) {
        for (Table table : tables) {
            if (table.getTableNumber().equals(tableNumber)) {
                // Create a new Order instance.
                Order order = new Order();
                // Extract the numeric part from the table number (e.g., "Bord 3" -> 3)
                String numericPart = tableNumber.replaceAll("\\D+", "");
                if (!numericPart.isEmpty()) {
                    order.setTableNumber(Integer.parseInt(numericPart));
                } else {
                    order.setTableNumber(0); // Fallback value
                }
                order.setIsFinished(false);
                // Initialize the list of OrderDishes
                List<OrderDish> orderDishes = new ArrayList<>();
                order.setOrderDishes(orderDishes);

                // Create an OrderDish for the provided dish name.
                OrderDish orderDish = new OrderDish();
                // Create a Dish object and set its name.
                Dish dishObj = new Dish();
                dishObj.setName(dish);
                orderDish.setDish(dishObj);
                orderDish.setStatus("PENDING");  // Initial status
                orderDishes.add(orderDish);

                table.addOrder(order);
                break;
            }
        }
    }
}
