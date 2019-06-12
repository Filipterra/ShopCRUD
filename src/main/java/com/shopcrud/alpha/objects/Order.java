package com.shopcrud.alpha.objects;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Order implements Serializable {

    @NotNull
    private int id = -1;
    @NotNull
    @Size(min = 2, message = "Product name must have at least two characters")
    private String productName = "";
    @Min(0)
    private BigDecimal price = BigDecimal.ZERO;
    @Min(value = 1, message = "Must order something")
    private int amount = 1;
    @NotNull
    @Size(min = 2, message = "Client name must have at least two characters")
    private String client = "";
    @NotNull
    private Status status = Status.PLACED;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
    	this.status=status;
    }
    
    public boolean isNewOrder() {
        return getId() == -1;
    }
    
    public String getClient() {
        return client;
    }

    public void setClient(String clientName) {
        this.client = clientName;
    }

}
