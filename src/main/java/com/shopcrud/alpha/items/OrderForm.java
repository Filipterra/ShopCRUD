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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.shopcrud.alpha.ProductCrudLogic;
import com.shopcrud.alpha.authentication.CurrentUser;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.objects.Product;

public class OrderForm extends Div {

    private VerticalLayout content;

    private TextField productName;
    private TextField count;
    private Button save;
    private Button discard;
    private Button cancel;

    private ProductCrudLogic viewLogic;
    private Binder<Order> binder;
    private Order currentOrder;
    private Product tmpProduct;
    private Order tmpOrder;

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

    public OrderForm(ProductCrudLogic sampleCrudLogic) {
    	
        setClassName("order-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = sampleCrudLogic;

        productName = new TextField("Product name");
        productName.setWidth("100%");
        productName.setValueChangeMode(ValueChangeMode.EAGER);
        productName.setRequired(true);
        content.add(productName);

        count = new TextField("Ordered");
        count.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        count.setValueChangeMode(ValueChangeMode.EAGER);
        count.setRequired(true);
        count.setEnabled(false);
        content.add(count);
        
        binder = new BeanValidationBinder<>(Order.class);
        binder.bind(productName, "productName");
        binder.forField(count).withConverter(new StockCountConverter())
                .bind("amount");
	
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            count.setEnabled(hasChanges);
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        save.addClickListener(event -> {
        	tmpProduct = DataService.get().getProductByName(productName.getValue());
        	int tmpVal=Integer.parseInt(count.getValue());
            if (tmpProduct==null) {
            	Notification.show("Unknown product");
            }
            else if (tmpVal<=0) {
            	Notification.show("Ivalid amount");
            }
            else {
            	tmpOrder=new Order();
            	tmpOrder.setAmount(tmpVal);
            	tmpOrder.setClient(CurrentUser.get());
            	tmpOrder.setPrice(new BigDecimal(tmpVal).multiply(tmpProduct.getPrice()));
            	tmpOrder.setProductName(tmpProduct.getProductName());
            	viewLogic.saveOrder(tmpOrder);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);
        save.setEnabled(false);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editOrder(currentOrder));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelOrder());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProduct())
                .setFilter("event.key == 'Escape'");

        content.add(save, discard, cancel);
        
    }


    public void editOrder(Order order) {
        
    	if (order == null) {
            order = new Order();
        }
        //product.setAvailability();
        currentOrder = order;
        binder.readBean(order);
        
    }
    
}
