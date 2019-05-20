package com.shopcrud.alpha.items;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.shopcrud.alpha.SampleCrudLogic;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Availability;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Product;

public class OrderForm extends Div {

    private VerticalLayout content;

    private TextField productName;
    private TextField count;
    private Button save;
    private Button discard;
    private Button cancel;

    private SampleCrudLogic viewLogic;
    private Binder<Product> binder;
    private Product currentProduct;
    private Product tmpProduct;

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

    public OrderForm(SampleCrudLogic sampleCrudLogic) {
    	
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
        content.add(count);
        
        binder = new BeanValidationBinder<>(Product.class);
        binder.bind(productName, "productName");
        binder.forField(count).withConverter(new StockCountConverter())
                .bind("stockCount");
	
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
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
            else if (tmpVal<=0 || tmpProduct.getStockCount()<tmpVal) {
            	Notification.show("Ivalid amount");
            }
            else {
            	tmpProduct.setStockCount(tmpProduct.getStockCount()-tmpVal);
            	tmpProduct.setAvailability();
            	viewLogic.saveOrder(tmpProduct);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);
        save.setEnabled(false);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editOrder(currentProduct));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelOrder());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProduct())
                .setFilter("event.key == 'Escape'");

        content.add(save, discard, cancel);
        
    }


    public void editOrder(Product product) {
        
    	if (product == null) {
            product = new Product();
        }
        product.setAvailability();
        currentProduct = product;
        binder.readBean(product);
        
    }
    
}
