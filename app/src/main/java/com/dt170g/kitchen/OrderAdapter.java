package com.dt170g.kitchen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.util.Log;

import com.dt170g.api.ApiService;
import com.dt170g.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Map;
import java.util.HashMap;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final List<Order> orderList;
    private final OrderManager orderManager;
    private final OnOrderReadyListener listener;

    public interface OnOrderReadyListener {
        void onOrderReady(Order order);
    }

    public OrderAdapter(List<Order> orderList, OnOrderReadyListener listener) {
        this.orderList = orderList;
        this.orderManager = OrderManager.getInstance();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tableNumber.setText("Table " + order.getTableNumber());

        // ✅ Determine readiness based on `OrderDish` statuses
        boolean starterReady = false;
        boolean mainCourseReady = false;
        boolean dessertReady = false;

        if (order.getOrderDishes() != null) {
            for (OrderDish dish : order.getOrderDishes()) {
                if (dish.getDishName().toLowerCase().contains("starter") && "READY".equals(dish.getStatus())) {
                    starterReady = true;
                }
                if (dish.getDishName().toLowerCase().contains("main") && "READY".equals(dish.getStatus())) {
                    mainCourseReady = true;
                }
                if (dish.getDishName().toLowerCase().contains("dessert") && "READY".equals(dish.getStatus())) {
                    dessertReady = true;
                }
            }
        }

        holder.starterCheckbox.setChecked(starterReady);
        holder.mainCourseCheckbox.setChecked(mainCourseReady);
        holder.dessertCheckbox.setChecked(dessertReady);

        // Set listeners to update dish statuses
        holder.starterCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateDishStatus(order, "starter", isChecked ? "READY" : "PENDING");
        });

        holder.mainCourseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateDishStatus(order, "main", isChecked ? "READY" : "PENDING");
        });

        holder.dessertCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateDishStatus(order, "dessert", isChecked ? "READY" : "PENDING");
        });

        // ✅ Ready button updates all dishes to "SERVED"
        holder.readyButton.setOnClickListener(v -> {
            for (OrderDish dish : order.getOrderDishes()) {
                dish.setStatus("SERVED");
            }
            notifyDataSetChanged(); // Refresh UI
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tableNumber;
        CheckBox starterCheckbox, mainCourseCheckbox, dessertCheckbox;
        Button readyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.tableNumber);
            starterCheckbox = itemView.findViewById(R.id.starterCheckbox);
            mainCourseCheckbox = itemView.findViewById(R.id.mainCourseCheckbox);
            dessertCheckbox = itemView.findViewById(R.id.dessertCheckbox);
            readyButton = itemView.findViewById(R.id.readyButton);
        }
    }
    private void updateDishStatus(Order order, String dishType, String newStatus) {

        ApiService apiService;
        apiService = RetrofitClient.getInstance().getApi();

        for (OrderDish dish : order.getOrderDishes()) {
            if (dish.getDishName().toLowerCase().contains(dishType)) {
                dish.setStatus(newStatus);
                notifyDataSetChanged();

                // Send API request to update status in backend
                Map<String, String> body = new HashMap<>();
                body.put("status", newStatus);
                apiService.updateOrderDishStatus(dish.getId(), body).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("OrderAdapter", "Dish " + dish.getDishName() + " updated to " + newStatus);
                        } else {
                            Log.e("OrderAdapter", "Failed to update dish status");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("OrderAdapter", "Network error: " + t.getMessage());
                    }
                });
                return;
            }
        }
    }

}
