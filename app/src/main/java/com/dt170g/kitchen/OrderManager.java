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

    public List<Table> getTables() { return tables; }

    public void addOrder(String tableNumber, String dish) {
        for (Table table : tables) {
            if (table.getTableNumber().equals(tableNumber)) {
                table.addOrder(new Order(tableNumber, dish));
                break;
            }
        }
    }
}
