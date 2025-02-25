

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

    public String getTableNumber() {
        return tableNumber;
    }

    public String getDish() {
        return dish;
    }
    public void setReady(boolean ready) {
        if (dish.equalsIgnoreCase("Förrätt")) {
            this.isStarterReady = ready;
        } else if (dish.equalsIgnoreCase("Huvudrätt")) {
            this.isMainCourseReady = ready;
        } else if (dish.equalsIgnoreCase("Efterrätt")) {
            this.isDessertReady = ready;
        }
    }


    public boolean isStarterReady() {
        return isStarterReady;
    }

    public void setStarterReady(boolean starterReady) {
        isStarterReady = starterReady;
    }

    public boolean isMainCourseReady() {
        return isMainCourseReady;
    }

    public void setMainCourseReady(boolean mainCourseReady) {
        isMainCourseReady = mainCourseReady;
    }

    public boolean isDessertReady() {
        return isDessertReady;
    }

    public void setDessertReady(boolean dessertReady) {
        isDessertReady = dessertReady;
    }
}


