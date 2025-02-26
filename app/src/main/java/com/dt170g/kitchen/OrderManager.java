package com.dt170g.kitchen;

import com.dt170g.api.ApiService;
import com.dt170g.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {
    private static OrderManager instance;
    private List<Order> orders;
    private Map<String, Table> tableMap; // Maps table numbers to Table objects
    private ApiService apiService;

    private OrderManager() {
        orders = new ArrayList<>();
        tableMap = new HashMap<>();
        apiService = RetrofitClient.getApiService();
        fetchOrdersFromAPI(); // Fetch orders on initialization
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public List<Order> getOrders() {
        return orders;
    }

    // Correctly implement addOrder()
    public void addOrder(String tableNumber, String dish) {
        if (!tableMap.containsKey(tableNumber)) {
            tableMap.put(tableNumber, new Table(tableNumber));
        }
        Table table = tableMap.get(tableNumber);
        Order newOrder = new Order(tableNumber, dish);
        table.addOrder(newOrder);
        orders.add(newOrder);

        // Send new order to API
        apiService.createOrder(newOrder).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchOrdersFromAPI(); // Refresh orders from server
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Fetch orders from API and populate tables
    private void fetchOrdersFromAPI() {
        apiService.getActiveOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orders.clear();
                    tableMap.clear(); // Reset tables

                    for (Order order : response.body()) {
                        orders.add(order);

                        // Add orders to respective tables
                        String tableNumber = order.getTableNumber();
                        if (!tableMap.containsKey(tableNumber)) {
                            tableMap.put(tableNumber, new Table(tableNumber));
                        }
                        tableMap.get(tableNumber).addOrder(order);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Mark an order as ready and update API
    public void markOrderReady(long orderId) {
        apiService.markOrderAsFinished(orderId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchOrdersFromAPI(); // Refresh orders after marking as ready
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Implementation of getTables() - returns all tables with their orders
    public List<Table> getTables() {
        return new ArrayList<>(tableMap.values());
    }
}
