package com.dt170g.kitchen.api;

import com.dt170g.kitchen.Order;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // Fetch all active orders (including dishes)
    @GET("api/kitchen/orders")
    Call<List<Map<String, Object>>> getActiveKitchenOrders();

    // Create a new order
    @POST("api/orders")
    Call<Map<String, Object>> createOrder(@Body Order order);

    // Update dish status - using Body instead of Query
    @PUT("api/kitchen/order-dish/{orderDishId}/status")
    Call<Void> updateOrderDishStatus(
            @Path("orderDishId") Long orderDishId,
            @Body Map<String, String> statusMap
    );
}