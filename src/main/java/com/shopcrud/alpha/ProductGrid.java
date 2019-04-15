package com.shopcrud.alpha;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.shopcrud.alpha.Category;
import com.shopcrud.alpha.Product;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.stream.Collectors;


public class ProductGrid extends Grid<Product> {

    public ProductGrid() {
        setSizeFull();

        addColumn(Product::getProductName)
                .setHeader("Product name")
                .setFlexGrow(20)
                .setSortable(true);

        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        final String priceTemplate = "<div style='text-align: right'>[[item.price]]</div>";
        addColumn(TemplateRenderer.<Product>of(priceTemplate)
                .withProperty("price", product -> decimalFormat.format(product.getPrice()) + " €"))
                .setHeader("Price")
                .setComparator(Comparator.comparing(Product::getPrice))
                .setFlexGrow(3);

        final String availabilityTemplate = "<div style='text-align: right'>[[item.avalibility]]</div>";
        addColumn(TemplateRenderer.<Product>of(availabilityTemplate)
                .withProperty("availability", product -> product.getAvailability().toString()))
                .setHeader("Availability")
                .setComparator(Comparator.comparing(Product::getAvailability))
                .setFlexGrow(5);

        final String stockCountTemplate = "<div style='text-align: right'>[[item.stockCount]]</div>";
        addColumn(TemplateRenderer.<Product>of(stockCountTemplate)
                .withProperty("stockCount", product -> product.getStockCount() == 0 ? "-" : Integer.toString(product.getStockCount())))
                .setHeader("Stock count")
                .setComparator(Comparator.comparingInt(Product::getStockCount))
                .setFlexGrow(3);

        addColumn(this::formatCategories)
                .setHeader("Category")
                .setFlexGrow(12);
                
    }

    public Product getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Product product) {
        getDataCommunicator().refresh(product);
    }

    private String formatCategories(Product product) {
        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            return "";
        }
        return product.getCategory().stream()
                .sorted(Comparator.comparing(Category::getId))
                .map(Category::getName).collect(Collectors.joining(", "));
    }
}
