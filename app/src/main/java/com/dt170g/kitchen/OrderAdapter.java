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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final List<Order> orderList;
    private final OnOrderReadyListener orderReadyListener;

    public interface OnOrderReadyListener {
        void onOrderReady(Order order);
    }

    public OrderAdapter(List<Order> orderList, OnOrderReadyListener listener) {
        this.orderList = orderList;
        this.orderReadyListener = listener;
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
        holder.tableNumber.setText(order.getTableNumber());

        // Set checkboxes based on order status
        holder.starterCheckbox.setChecked(order.isStarterReady());
        holder.mainCourseCheckbox.setChecked(order.isMainCourseReady());
        holder.dessertCheckbox.setChecked(order.isDessertReady());

        // Handle button click to mark order as ready
        holder.readyButton.setOnClickListener(v -> {
            if (holder.starterCheckbox.isChecked()) {
                order.markStarterReady();
            }
            if (holder.mainCourseCheckbox.isChecked()) {
                order.markMainCourseReady();
            }
            if (holder.dessertCheckbox.isChecked()) {
                order.markDessertReady();
            }

            // Send update to API
            OrderManager.getInstance().markOrderReady(order.getId());

            // Notify listener that order has changed
            orderReadyListener.onOrderReady(order);
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
}
