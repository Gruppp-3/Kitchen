package com.dt170g.kitchen;

import android.util.Log;
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

    private final List<RecievedOrder> orderList;
    private final OnOrderReadyListener listener;

    // This interface notifies KitchenActivity when “Markera Order Som Klar” is pressed
    public interface OnOrderReadyListener {
        void onOrderReady(RecievedOrder order, Boolean starter, Boolean main, Boolean dessert);
    }

    public OrderAdapter(List<RecievedOrder> orderList, OnOrderReadyListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        RecievedOrder order = orderList.get(position);

        // Set the table number
        holder.tableNumber.setText("Bord: " + order.getTableNumber());

        // We'll build separate text for appetizer, main, and dessert
        StringBuilder appetizerList = new StringBuilder();
        StringBuilder mainCourseList = new StringBuilder();
        StringBuilder dessertList = new StringBuilder();

        // If the order has orderSpecs, let's parse them
        // If the order has orderSpecs, let's parse them
        if (order.getOrderSpecs() != null) {
            for (OrderSpecs dish : order.getOrderSpecs()) {
                Log.d("ORDER_CHECK", "Rätt: " + dish.getMeal() + ", Kategori: " + dish.getCategory());

                // We do a case-insensitive check for the category
                String category = (dish.getCategory() != null) ? dish.getCategory().toLowerCase() : "";

                if (category.contains("förrätt")) {
                    appetizerList.append(dish.getMeal()).append(" (x").append(dish.getCount()).append(")\n");
                }
                else if (category.contains("huvudrätt") || category.contains("varmrätt")) {
                    mainCourseList.append(dish.getMeal()).append(" (x").append(dish.getCount()).append(")\n");
                }
                else if (category.contains("efterrätt")) {
                    dessertList.append(dish.getMeal()).append(" (x").append(dish.getCount()).append(")\n");
                }
                // Handle Vegetariska category
                else if (category.contains("vegetarisk")) {
                    // Add to main course list with a "Veg" indicator
                    mainCourseList.append(dish.getMeal()).append(" (x").append(dish.getCount()).append(") [Veg]\n");
                }
                // Handle other unknown categories
                else {
                    // Add to main course with unknown category indicator
                    mainCourseList.append(dish.getMeal()).append(" (x").append(dish.getCount()).append(")\n");
                }
            }
        }

        // If nothing was added, show “Inga …”
        holder.orderedAppetizer.setText(
                appetizerList.length() > 0 ? appetizerList.toString() : "Inga förrätter"
        );
        holder.orderedMainCourse.setText(
                mainCourseList.length() > 0 ? mainCourseList.toString() : "Inga huvudrätter"
        );
        holder.orderedDessert.setText(
                dessertList.length() > 0 ? dessertList.toString() : "Inga efterrätter"
        );

        // Set button click to notify the Activity
        holder.readyButton.setOnClickListener(v -> {
            boolean starterChecked = holder.starterCheckbox.isChecked();
            boolean mainChecked = holder.mainCourseCheckbox.isChecked();
            boolean dessertChecked = holder.dessertCheckbox.isChecked();

            // Let the activity decide what to do
            listener.onOrderReady(order, starterChecked, mainChecked, dessertChecked);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Optional: remove an order from the list and refresh
    public void removeOrder(RecievedOrder order) {
        int index = orderList.indexOf(order);
        if (index != -1) {
            orderList.remove(index);
            notifyItemRemoved(index);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Views from item_order.xml
        TextView tableNumber, orderedAppetizer, orderedMainCourse, orderedDessert;
        CheckBox starterCheckbox, mainCourseCheckbox, dessertCheckbox;
        Button readyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tableNumber = itemView.findViewById(R.id.tableNumber);
            orderedAppetizer = itemView.findViewById(R.id.orderedAppetizer);
            orderedMainCourse = itemView.findViewById(R.id.orderedMainCourse);
            orderedDessert = itemView.findViewById(R.id.orderedDessert);

            starterCheckbox = itemView.findViewById(R.id.starterCheckbox);
            mainCourseCheckbox = itemView.findViewById(R.id.mainCourseCheckbox);
            dessertCheckbox = itemView.findViewById(R.id.dessertCheckbox);

            readyButton = itemView.findViewById(R.id.readyButton);
        }
    }
}
