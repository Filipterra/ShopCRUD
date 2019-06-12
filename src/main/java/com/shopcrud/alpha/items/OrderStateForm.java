package com.shopcrud.alpha.items;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.shopcrud.alpha.OrderCrudLogic;
import com.shopcrud.alpha.authentication.BasicAccessControl;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.objects.Status;

public class OrderStateForm extends Div {

    private VerticalLayout content;

    private TextField productName;
    private TextField price;
    private TextField amount;
    private TextField client;
    private Button accept;
    private Button reject;
    private Button cancel;

    private OrderCrudLogic viewLogic;
    private Binder<Order> binder;
    private Order currentOrder;

    private static class PriceConverter extends StringToBigDecimalConverter {

        public PriceConverter() {
            super(BigDecimal.ZERO, "Cannot convert value to a number.");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            NumberFormat format = super.getFormat(locale);
            if (format instanceof DecimalFormat) {
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);
            }
            return format;
        }
    }

    private static class StockCountConverter extends StringToIntegerConverter {

        public StockCountConverter() {
            super(0, "Could not convert value to " + Integer.class.getName()
                    + ".");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }
    }

    public OrderStateForm(OrderCrudLogic sampleCrudLogic) {
        setClassName("order_state-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = sampleCrudLogic;

        productName = new TextField("Product name");
        productName.setWidth("100%");
        productName.setReadOnly(true);
        content.add(productName);

        price = new TextField("Price");
        price.setSuffixComponent(new Span("€"));
        price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        price.setReadOnly(true);

        amount = new TextField("Amount");
        amount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        amount.setReadOnly(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout(amount, price);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, amount, price);
        content.add(horizontalLayout);
        
        client = new TextField("Client");
        client.setWidth("100%");
        client.setReadOnly(true);
        content.add(client);

        binder = new BeanValidationBinder<>(Order.class);
        binder.forField(price).withConverter(new PriceConverter())
                .bind("price");
        binder.forField(amount).withConverter(new StockCountConverter())
                .bind("amount");
        binder.bindInstanceFields(this);

        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            if (BasicAccessControl.isUserAdmin()) accept.setEnabled(isValid && currentOrder.getStatus().equals(Status.PLACED));
            reject.setEnabled(isValid && currentOrder.getStatus().equals(Status.PLACED));
        });
        
        if (BasicAccessControl.isUserAdmin()) {
        accept = new Button("Accept order");
        accept.setWidth("100%");
        accept.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        accept.addClickListener(event -> {
            if (currentOrder != null) {
            	if (currentOrder.getAmount()>DataService.get().getProductByName(currentOrder.getProductName()).getStockCount())	{
            		Notification.show("Not enough products in stock.");
            	}
            	else viewLogic.acceptOrder(currentOrder);
            }
        });
        accept.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);
        }
        
        reject = new Button("Cancel order");
        reject.setWidth("100%");
        reject.addClickListener(event -> {
            if (currentOrder != null) {
            	viewLogic.cancelOrder(currentOrder);
            }
        });

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelOrder());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelOrder())
                .setFilter("event.key == 'Escape'");
        if (BasicAccessControl.isUserAdmin()) content.add(accept);
        content.add(reject,  cancel);
    }

    public void editOrder(Order order) {
        if (order == null) {
            order = new Order();
        }
        currentOrder = order;
        binder.readBean(order);
    }
}
