package com.dt170g.kitchen;
import com.google.gson.annotations.SerializedName;

public class Order {
    private static long idCounter = 0;
    @SerializedName("id")
    private long id; // Unique ID for order tracking
    @SerializedName("tableNumber")
    private String tableNumber;
    @SerializedName("dish")
    private String dish;
    @SerializedName("starterReady")
    private boolean isStarterReady;
    @SerializedName("mainCourseReady")
    private boolean isMainCourseReady;
    @SerializedName("dessertReady")
    private boolean isDessertReady;

    public Order(String tableNumber, String dish) {
        this.id = ++idCounter; // Auto-generate unique ID
        this.tableNumber = tableNumber;
        this.dish = dish;
        this.isStarterReady = false;
        this.isMainCourseReady = false;
        this.isDessertReady = false;
    }

    // Getter for Order ID (used for API communication)
    public long getId() {
        return id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public String getDish() {
        return dish;
    }

    // Update order status using the correct methods
    public void markStarterReady() {
        if (dish.equalsIgnoreCase("Förrätt")) {
            this.isStarterReady = true;
        }
    }

    public void markMainCourseReady() {
        if (dish.equalsIgnoreCase("Huvudrätt")) {
            this.isMainCourseReady = true;
        }
    }

    public void markDessertReady() {
        if (dish.equalsIgnoreCase("Efterrätt")) {
            this.isDessertReady = true;
        }
    }

    public boolean isStarterReady() {
        return isStarterReady;
    }

    public boolean isMainCourseReady() {
        return isMainCourseReady;
    }

    public boolean isDessertReady() {
        return isDessertReady;
    }
}
