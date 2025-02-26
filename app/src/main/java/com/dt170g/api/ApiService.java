package com.dt170g.api;

import com.dt170g.kitchen.Order;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Fetch all active orders
    @GET("/api/orders/active")
    Call<List<Order>> getActiveOrders();

    // Create a new order (used by the waiter app)
    @POST("/api/orders")
    Call<Order> createOrder(@Body Order order);

    // Mark an order as finished
    @PUT("/api/orders/{id}/finish")
    Call<Void> markOrderAsFinished(@Path("id") long orderId);
}
