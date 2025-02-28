package com.dt170g.kitchen;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RecievedOrder {

    @JsonProperty("tableNumber")
    private Integer tableNumber;

    @JsonProperty("orderSpecs")
    private List<OrderSpecs> orderSpecs;

    @JsonProperty("isFinished")
    private Boolean isFinished;

    // Getters
    public Integer getTableNumber() {
        return tableNumber;
    }

    public List<OrderSpecs> getOrderSpecs() {
        return orderSpecs;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    // Setters
    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setOrderSpecs(List<OrderSpecs> orderSpecs) {
        this.orderSpecs = orderSpecs;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }
}
