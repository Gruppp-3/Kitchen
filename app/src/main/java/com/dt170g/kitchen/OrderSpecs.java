package com.dt170g.kitchen;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderSpecs {
    @JsonProperty("category")
    private String category;

    @JsonProperty("meal")
    private String meal;

    @JsonProperty("count")
    private Integer count;

    // Getters
    public String getCategory() {
        return category;
    }

    public String getMeal() {
        return meal;
    }

    public Integer getCount() {
        return count;
    }

    // Setters
    public void setCategory(String category) {
        this.category = category;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
