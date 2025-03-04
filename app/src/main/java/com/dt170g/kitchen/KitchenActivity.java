package com.dt170g.kitchen;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dt170g.api.ApiService;
import com.dt170g.api.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KitchenActivity extends AppCompatActivity implements OrderAdapter.OnOrderReadyListener {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<RecievedOrder> orderList;  // The list of orders from the DB
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_screen);

        // 1) Setup the RecyclerView
        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2) Get the API service
        apiService = RetrofitClient.getInstance().getApi();

        // 3) Fetch orders right away
        fetchActiveOrders();
    }

    private void fetchActiveOrders() {
        apiService.getActiveOrders().enqueue(new Callback<List<RecievedOrder>>() {
            @Override
            public void onResponse(Call<List<RecievedOrder>> call, Response<List<RecievedOrder>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList = response.body();

                    // Create and attach our adapter
                    orderAdapter = new OrderAdapter(orderList, KitchenActivity.this);
                    recyclerView.setAdapter(orderAdapter);

                    // Debug log the JSON we received
                    Log.d("API_RESPONSE", "Orders from DB: " + new Gson().toJson(orderList));
                } else {
                    Toast.makeText(KitchenActivity.this, "No orders found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecievedOrder>> call, Throwable t) {
                Toast.makeText(KitchenActivity.this, "Error fetching orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Called when the adapter's “Markera Order Som Klar” button is clicked.
     * For now, we just display a Toast to prove we got the click.
     */
    @Override
    public void onOrderReady(RecievedOrder order, Boolean starter, Boolean main, Boolean dessert) {
        String msg = "Bord " + order.getTableNumber()
                + ": Förrätt=" + starter
                + ", Huvudrätt=" + main
                + ", Efterrätt=" + dessert;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        // If you want to remove the order from the list, do it here:
        // orderAdapter.removeOrder(order);

        // Or if you want to call the backend to signal the staff:
        // apiService.sendSignal(order.getTableNumber(), starter, main, dessert)...
    }
}
