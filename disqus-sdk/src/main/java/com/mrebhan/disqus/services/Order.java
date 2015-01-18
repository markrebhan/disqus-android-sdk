package com.mrebhan.disqus.services;

public enum Order {
    DESCENDING("desc"),
    ASCENDING("asc");

    private Order(String order) {
        this.orderString = order;
    }

    private String orderString;

    public String toString(Order order) {
        return order.orderString;
    }

}
