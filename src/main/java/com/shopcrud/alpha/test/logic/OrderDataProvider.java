package com.shopcrud.alpha.test.logic;

import java.util.Locale;
import java.util.Objects;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.shopcrud.alpha.test.logic.DataService;
import com.shopcrud.alpha.objects.Order;

public class OrderDataProvider extends ListDataProvider<Order> {

    private String filterText = "";

    public OrderDataProvider() {
        super(DataService.get().getAllOrders());
    }

    public void save(Order order) {
        boolean newOrder = order.isNewOrder();

        DataService.get().updateOrder(order);
        if (newOrder) {
            refreshAll();
        } else {
            refreshItem(order);
        }
    }

    public void delete(Order order) {
        DataService.get().deleteOrder(order.getId());
        refreshAll();
    }

    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();

        setFilter(order -> passesFilter(order.getProductName(), filterText)
                || passesFilter(order.getClient(), filterText));
    }
	
    @Override
    public Integer getId(Order order) {
        Objects.requireNonNull(order,
                "Cannot provide an id for a null order.");

        return order.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText.toLowerCase(Locale.ENGLISH));
    }

    public void setClientFilter() {
    	setFilter(order -> loggedClient(order));
    }
    
    private boolean loggedClient (Order order) {
    	return order!=null && order.getClient().equals("tester");
    }
}
