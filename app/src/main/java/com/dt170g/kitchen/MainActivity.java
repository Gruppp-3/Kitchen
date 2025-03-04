package com.dt170g.kitchen;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dt170g.api.ApiService;
import com.dt170g.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OrderAdapter.OnOrderReadyListener {
    private static final String TAG = "MainActivity";
    private static final int REFRESH_INTERVAL = 30000; // 30 seconds

    private RecyclerView upcomingOrdersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<RecievedOrder> receivedOrders;
    private ApiService apiService;
    private Handler refreshHandler;
    private Runnable refreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize API Service
        apiService = RetrofitClient.getInstance().getApi();

        // Initialize RecyclerView
        initializeRecyclerView();

        // Start fetching orders
        fetchOrders();

        // Setup periodic refresh
        setupPeriodicRefresh();
    }

    private void initializeRecyclerView() {
        upcomingOrdersRecyclerView = findViewById(R.id.upcomingOrdersRecyclerView);
        receivedOrders = new ArrayList<>();
        orderAdapter = new OrderAdapter(receivedOrders, this);

        upcomingOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingOrdersRecyclerView.setAdapter(orderAdapter);
    }

    private void fetchOrders() {
        apiService.getActiveOrders().enqueue(new Callback<List<RecievedOrder>>() {
            @Override
            public void onResponse(Call<List<RecievedOrder>> call, Response<List<RecievedOrder>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    receivedOrders.clear();
                    receivedOrders.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();

                    Log.d(TAG, "Fetched " + receivedOrders.size() + " active orders");
                } else {
                    Log.e(TAG, "Could not fetch orders: " + response.message());
                    showErrorToast("Kunde inte hämta ordrar. Försök igen senare.");
                }
            }

            @Override
            public void onFailure(Call<List<RecievedOrder>> call, Throwable t) {
                Log.e(TAG, "Network error when fetching orders", t);
                showErrorToast("Nätverksfel. Kontrollera din anslutning.");
            }
        });
    }

    private void setupPeriodicRefresh() {
        refreshHandler = new Handler(Looper.getMainLooper());
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                fetchOrders();
                refreshHandler.postDelayed(this, REFRESH_INTERVAL);
            }
        };
        refreshHandler.postDelayed(refreshRunnable, REFRESH_INTERVAL);
    }

    @Override
    public void onOrderReady(RecievedOrder order, Boolean starter, Boolean main, Boolean dessert) {
        apiService.sendSignal(order.getTableNumber(), starter, main, dessert)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Order signal sent successfully for table " + order.getTableNumber());
                            fetchOrders(); // Refresh orders after marking

                            // Show toast message to staff
                            Toast.makeText(MainActivity.this,
                                    "Signal skickad till personalen för " + order.getTableNumber(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Network error when sending order signal", t);
                        showErrorToast("Nätverksfel. Kunde inte uppdatera ordern.");
                    }
                });
    }

    private void showErrorToast(String message) {
        runOnUiThread(() ->
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leaks
        if (refreshHandler != null && refreshRunnable != null) {
            refreshHandler.removeCallbacks(refreshRunnable);
        }
    }
}