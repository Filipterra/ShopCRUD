package com.shopcrud.alpha.items;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Product;

import java.util.Comparator;
import java.util.stream.Collectors;


public class TresholdGrid extends Grid<Product> {

    public TresholdGrid() {
    	
        setSizeFull();

        addColumn(Product::getProductName)
                .setHeader("Product name")
                .setFlexGrow(20)
                .setSortable(true);

        final String stockCountTemplate = "<div style='text-align: right'>[[item.stockCount]]</div>";
        addColumn(TemplateRenderer.<Product>of(stockCountTemplate)
                .withProperty("stockCount", product -> product.getStockCount() == 0 ? "-" : Integer.toString(product.getStockCount())))
                .setHeader("Stock count")
                .setComparator(Comparator.comparingInt(Product::getStockCount))
                .setFlexGrow(3);
        
        final String individualLimitTemplate = "<div style='text-align: right'>[[item.individualLimit]]</div>";
        addColumn(TemplateRenderer.<Product>of(individualLimitTemplate)
                .withProperty("individualLimit", product -> product.getIndividualLimit()))
                .setHeader("Treshold")
                .setComparator(Comparator.comparing(Product::getIndividualLimit))
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
