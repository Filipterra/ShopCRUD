package com.shopcrud.alpha.objects;

public enum Availability {
    AVAILABLE("Available"), LAST_ITEMS("Last Items"), UNAVAILABLE("Unavailable");

    private final String name;

    private Availability(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
