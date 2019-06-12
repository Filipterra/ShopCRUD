package com.shopcrud.alpha.items;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.shopcrud.alpha.authentication.BasicAccessControl;
import com.shopcrud.alpha.objects.Order;
import java.text.DecimalFormat;
import java.util.Comparator;


public class OrderGrid extends Grid<Order> {

    public OrderGrid() {
        setSizeFull();

        addColumn(Order::getProductName)
                .setHeader("Product name")
                .setFlexGrow(20)
                .setSortable(true);

        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        final String priceTemplate = "<div style='text-align: right'>[[item.price]]</div>";
        addColumn(TemplateRenderer.<Order>of(priceTemplate)
                .withProperty("price", order -> decimalFormat.format(order.getPrice()) + " €"))
                .setHeader("Price")
                .setComparator(Comparator.comparing(Order::getPrice))
                .setFlexGrow(3);

        final String statusTemplate = "<div style='text-align: right'>[[item.status]]</div>";
        addColumn(TemplateRenderer.<Order>of(statusTemplate)
                .withProperty("status", order -> order.getStatus().toString()))
                .setHeader("Status")
                .setComparator(Comparator.comparing(Order::getStatus))
                .setFlexGrow(3);

        final String amountTemplate = "<div style='text-align: right'>[[item.amount]]</div>";
        addColumn(TemplateRenderer.<Order>of(amountTemplate)
                .withProperty("amount", order -> order.getAmount() == 0 ? "-" : Integer.toString(order.getAmount())))
                .setHeader("Amount")
                .setComparator(Comparator.comparingInt(Order::getAmount))
                .setFlexGrow(3);
        
        if (BasicAccessControl.isUserAdmin()) {
        final String clientTemplate = "<div style='text-align: right'>[[item.client]]</div>";
        addColumn(TemplateRenderer.<Order>of(clientTemplate)
                .withProperty("client", order -> order.getClient()))
                .setHeader("Client")
                .setComparator(Comparator.comparing(Order::getClient))
                .setFlexGrow(3);
        }
                
    }

    public Order getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Order order) {
        getDataCommunicator().refresh(order);
    }
}
