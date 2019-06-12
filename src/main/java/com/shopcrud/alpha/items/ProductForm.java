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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.shopcrud.alpha.ProductCrudLogic;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Product;

public class ProductForm extends Div {

    private VerticalLayout content;

    private TextField productName;
    private TextField price;
    private TextField stockCount;
    private TextField individualLimit;
    private CheckboxGroup<Category> category;
    private Button save;
    private Button discard;
    private Button cancel;
    private Button delete;

    private ProductCrudLogic viewLogic;
    private Binder<Product> binder;
    private Product currentProduct;

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

    public ProductForm(ProductCrudLogic sampleCrudLogic) {
        setClassName("product-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = sampleCrudLogic;

        productName = new TextField("Product name");
        productName.setWidth("100%");
        productName.setRequired(true);
        productName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(productName);

        price = new TextField("Price");
        price.setSuffixComponent(new Span("€"));
        price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        price.setValueChangeMode(ValueChangeMode.EAGER);

        stockCount = new TextField("In stock");
        stockCount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        stockCount.setValueChangeMode(ValueChangeMode.EAGER);

        HorizontalLayout horizontalLayout = new HorizontalLayout(price,
                stockCount);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, price, stockCount);
        content.add(horizontalLayout);
        
        individualLimit = new TextField("Individual limit");
        individualLimit.setWidth("100%");
        individualLimit.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(individualLimit);

        category = new CheckboxGroup<>();
        category.setLabel("Categories");
        category.setId("category");
        category.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        category.setRequired(true);
        content.add(category);

        binder = new BeanValidationBinder<>(Product.class);
        binder.forField(price).withConverter(new PriceConverter())
                .bind("price");
        binder.forField(stockCount).withConverter(new StockCountConverter())
                .bind("stockCount");
        binder.forField(individualLimit).withConverter(new StockCountConverter())
        		.bind("individualLimit");
        binder.bindInstanceFields(this);

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
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
            	viewLogic.saveProduct(currentProduct);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editProduct(currentProduct));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelProduct());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProduct())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentProduct != null) {
                viewLogic.deleteProduct(currentProduct);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void setCategories(Collection<Category> categories) {
        category.setItems(categories);
    }

    public void editProduct(Product product) {
        if (product == null) {
            product = new Product();
        }
        delete.setVisible(!product.isNewProduct());
        currentProduct = product;
        binder.readBean(product);
    }
}
