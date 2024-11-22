package it.intesys.orderservice.entity;

public enum OrderStatus {
    CREATED("CREATED"),
    SHIPPING("SHIIPPING"),
    SHIPPED("SHIPED");

    private final String value;

    OrderStatus() {

        this.value = this.name();
    }

    OrderStatus(String value) {

        this.value = value;
    }

    public String getValue() {

        return value;
    }
}
