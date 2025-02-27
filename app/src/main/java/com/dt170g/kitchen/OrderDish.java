package com.dt170g.kitchen;

public class OrderDish {
    private Long id;
    private Dish dish;
    private String noteText;
    private String status; // e.g., "PENDING", "READY", "SERVED"

    // Helper method to get name of dish
    public String getDishName() {
        return dish != null ? dish.getName() : "";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Dish getDish() {
        return dish;
    }
    public void setDish(Dish dish) {
        this.dish = dish;
    }
    public String getNoteText() {
        return noteText;
    }
    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
