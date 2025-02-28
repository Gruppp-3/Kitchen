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

    private final List<RecievedOrder> orderList;
    private final OnOrderReadyListener listener;

    public interface OnOrderReadyListener {
        void onOrderReady(RecievedOrder order);
    }

    public OrderAdapter(List<RecievedOrder> orderList, OnOrderReadyListener listener) {
        this.orderList = orderList;
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
        RecievedOrder order = orderList.get(position);
        holder.tableNumber.setText("Bord: " + order.getTableNumber());

        StringBuilder appetizerList = new StringBuilder();
        StringBuilder mainCourseList = new StringBuilder();
        StringBuilder dessertList = new StringBuilder();

        if (order.getOrderSpecs() != null) {
            for (OrderSpecs dish : order.getOrderSpecs()) {
                Log.d("ORDER_CHECK", "Rätt: " + dish.getMeal() + ", Kategori: " + dish.getCategory());

                if ("Förrätt".equalsIgnoreCase(dish.getCategory())) {
                    appetizerList.append(dish.getMeal()).append(" (").append(dish.getCount()).append(")\n");
                } else if ("Huvudrätt".equalsIgnoreCase(dish.getCategory())) {
                    mainCourseList.append(dish.getMeal()).append(" (").append(dish.getCount()).append(")\n");
                } else if ("Efterrätt".equalsIgnoreCase(dish.getCategory())) {
                    dessertList.append(dish.getMeal()).append(" (").append(dish.getCount()).append(")\n");
                }
            }
        }

        holder.orderedAppetizer.setText(appetizerList.length() > 0 ? appetizerList.toString() : "Inga förrätter");
        holder.orderedMainCourse.setText(mainCourseList.length() > 0 ? mainCourseList.toString() : "Inga huvudrätter");
        holder.orderedDessert.setText(dessertList.length() > 0 ? dessertList.toString() : "Inga efterrätter");


        holder.readyButton.setOnClickListener(v -> {
            listener.onOrderReady(order);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tableNumber, orderedAppetizer, orderedMainCourse, orderedDessert;
        Button readyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.tableNumber);
            orderedAppetizer = itemView.findViewById(R.id.orderedAppetizer);
            orderedMainCourse = itemView.findViewById(R.id.orderedMainCourse);
            orderedDessert = itemView.findViewById(R.id.orderedDessert);
            readyButton = itemView.findViewById(R.id.readyButton);
        }
    }
}
