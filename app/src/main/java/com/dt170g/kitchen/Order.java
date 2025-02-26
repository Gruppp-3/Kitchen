package com.dt170g.kitchen;

public class Order {
    private String tableNumber;
    private String dish;
    private boolean isStarterReady;
    private boolean isMainCourseReady;
    private boolean isDessertReady;

    public Order(String tableNumber, String dish) {
        this.tableNumber = tableNumber;
        this.dish = dish;
        this.isStarterReady = false;
        this.isMainCourseReady = false;
        this.isDessertReady = false;
    }

    // Getter methods
    public String getTableNumber() {
        return tableNumber;
    }

    public String getDish() {
        return dish;
    }

    // Setter methods for dish readiness
    public void markStarterReady() {
        this.isStarterReady = true;
    }

    public void markMainCourseReady() {
        this.isMainCourseReady = true;
    }

    public void markDessertReady() {
        this.isDessertReady = true;
    }

    // Getter methods for checking readiness
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
