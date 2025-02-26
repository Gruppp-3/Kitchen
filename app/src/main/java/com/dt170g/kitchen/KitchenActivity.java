package com.dt170g.kitchen;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class KitchenActivity extends AppCompatActivity implements OrderAdapter.OnOrderReadyListener {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_screen);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ADD TEST ORDERS HERE
        addTestOrders();

        // Hämta beställningar från alla bord
        orderList = new ArrayList<>();
        for (Table table : OrderManager.getInstance().getTables()) {
            orderList.addAll(table.getOrders());
        }

        orderAdapter = new OrderAdapter(orderList, this);
        recyclerView.setAdapter(orderAdapter);

    }

    private void addTestOrders() {
        OrderManager orderManager = OrderManager.getInstance();
        orderManager.addOrder("Bord 1", "Förrätt");
        orderManager.addOrder("Bord 1", "Huvudrätt");
        orderManager.addOrder("Bord 2", "Efterrätt");
        orderManager.addOrder("Bord 3", "Huvudrätt");
    }


    @Override
    public void onOrderReady(Order order) {
        Toast.makeText(this, "Rätt från " + order.getTableNumber() + " är klar!", Toast.LENGTH_SHORT).show();
        sendSignalToPersonalApp(order);
        orderAdapter.notifyDataSetChanged();
    }

    private void sendSignalToPersonalApp(Order order) {
        // Här kan du implementera en signal via Firebase eller API
        Toast.makeText(this, "Signal skickad till personalen för " + order.getTableNumber(), Toast.LENGTH_SHORT).show();
    }
}
