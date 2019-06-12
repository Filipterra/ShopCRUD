package com.shopcrud.alpha.objects;

public enum Status {
    PLACED("Placed"), ACCEPTED("Accepted"), CANCELLED("Cancelled");

    private final String name;

    private Status(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
