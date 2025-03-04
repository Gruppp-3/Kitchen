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
    private List<RecievedOrder> orderList;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_screen);

        recyclerView = findViewById(R.id.recyclerViewReadyOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        apiService = RetrofitClient.getInstance().getApi();


        fetchActiveOrders();
    }

    private void fetchActiveOrders() {
        apiService.getActiveOrders().enqueue(new Callback<List<RecievedOrder>>() {
            @Override
            public void onResponse(Call<List<RecievedOrder>> call, Response<List<RecievedOrder>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList = response.body();
                    orderAdapter = new OrderAdapter(orderList, KitchenActivity.this);
                    recyclerView.setAdapter(orderAdapter);
                    Log.d("API_RESPONSE", new Gson().toJson(orderList));
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



    @Override
    public void onOrderReady(RecievedOrder order, Boolean starter, Boolean main, Boolean dessert) {
        Toast.makeText(this, "Maträtt för bord Nr " + order.getTableNumber() + " är klar!", Toast.LENGTH_SHORT).show();
        sendSignalToPersonalApp(order.getTableNumber(), starter, main, dessert);
        orderAdapter.notifyDataSetChanged();
    }

    private void sendSignalToPersonalApp(Integer tableNr, Boolean starter, Boolean main, Boolean dessert) {
        apiService.sendSignal(tableNr, starter, main, dessert).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(KitchenActivity.this, "Signal skickad till personalen för bord Nr " + tableNr, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
               Toast.makeText(KitchenActivity.this,
                       "Fel vid skicka signal: " + t.getMessage(),
                       Toast.LENGTH_SHORT).show();
            }
        });
    }
}
