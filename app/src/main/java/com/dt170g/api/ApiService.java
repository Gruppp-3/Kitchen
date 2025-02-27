package com.dt170g.api;
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

    // Fetch active orders (including their dishes) from the backend.
    @GET("api/kitchen/orders")
    Call<List<Order>> getActiveOrders();

    // Update the status of a specific order dish.
    @PUT("api/kitchen/order-dish/{orderDishId}/status")
    Call<Void> updateOrderDishStatus(@Path("orderDishId") Long orderDishId,
                                     @Body Map<String, String> statusMap);

    // Create a new order.
    @POST("api/orders")
    Call<Order> createOrder(@Body Order order);
}
