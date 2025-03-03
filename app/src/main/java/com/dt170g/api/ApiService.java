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
import retrofit2.http.Query;

import com.dt170g.kitchen.RecievedOrder;


public interface ApiService {


    @GET("api/kitchen/orders")
    Call<List<RecievedOrder>> getActiveOrders();

    @PUT("api/kitchen/order-dish/{orderDishId}/status")
    Call<Void> updateOrderDishStatus(@Path("orderDishId") Long orderDishId,
                                     @Body Map<String, String> statusMap);


    @POST("api/orders")
    Call<Order> createOrder(@Body Order order);

    @POST("api/kitchen/signal")
    Call<Void> sendSignal(@Query("tableNr") Integer tableNr, @Query("starter")Boolean starter, @Query("main") Boolean main, @Query("dessert") Boolean dessert);

}
