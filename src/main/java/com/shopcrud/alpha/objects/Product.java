package com.shopcrud.alpha.objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shopcrud.alpha.authentication.BasicAccessControl;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Availability;
import com.vaadin.flow.component.notification.Notification;

public class Product implements Serializable {

    @NotNull
    private int id = -1;
    @NotNull
    @Size(min = 2, message = "Product name must have at least two characters")
    private String productName = "";
    @Min(0)
    private BigDecimal price = BigDecimal.ZERO;
    @Size(min=1, message = "Product must have at least one category")
    private Set<Category> category;
    @Min(value = 0, message = "Can't have negative amount in stock")
    private int stockCount = 0;
    @Min(value = 0, message = "Can't have negative limit")
    private int individualLimit=0;
    @NotNull
    private Availability availability = Availability.UNAVAILABLE;
    @Min(value = 0, message = "Can't have negative limit")
    static Integer Limit=10;

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

    public Set<Category> getCategory() {
        return category;
    }

    public void setCategory(Set<Category> category) {
        this.category = category;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public void setAvailability() {
    	if (BasicAccessControl.isUserAdmin() && stockCount<individualLimit) {
    		Notification.show("Warning! " + productName + "has fallen below given treshold");
    	}
    	if (stockCount==0)
    	{
    		this.availability=Availability.UNAVAILABLE;
    	}
    	else if (stockCount<Limit) {
    		this.availability=Availability.LAST_ITEMS;
    		if (BasicAccessControl.isUserAdmin()) Notification.show(productName + " shortage");
    	}
    	else this.availability=Availability.AVAILABLE;
    }
    
    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }
    
    public int getIndividualLimit() {
    	return individualLimit;
    }
    
    public void setIndividualLimit(int newLimit) {
    	this.individualLimit=newLimit;
    }

    public boolean isNewProduct() {
        return getId() == -1;
    }
    
    static public void setLimit(int newVal) {
    	if(Limit!=newVal && newVal>=0)
    	{
    	Limit=newVal;
    	DataService.get().updateAvailability();
    	}
    }
    
    static public Integer getLimit() {
    	return Limit;
    }

}
