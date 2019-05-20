package com.shopcrud.alpha.data;

import java.util.Locale;
import java.util.Objects;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Product;

public class ProductDataProvider extends ListDataProvider<Product> {

    private String filterText = "";

    public ProductDataProvider() {
        super(DataService.get().getAllProducts());
    }

    public void save(Product product) {
        boolean newProduct = product.isNewProduct();

        DataService.get().updateProduct(product);
        if (newProduct) {
            refreshAll();
        } else {
            refreshItem(product);
        }
    }

    public void delete(Product product) {
        DataService.get().deleteProduct(product.getId());
        refreshAll();
    }

    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();

        setFilter(product -> passesFilter(product.getProductName(), filterText)
                || passesFilter(product.getAvailability(), filterText)
                || passesFilter(product.getCategory(), filterText));
    }
    
    public void setLimitFilter() {
    	setFilter(product -> belowTreshold(product));
    }
	
    @Override
    public Integer getId(Product product) {
        Objects.requireNonNull(product,
                "Cannot provide an id for a null product.");

        return product.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
    
    private boolean belowTreshold (Product product) {
    	return product!=null && product.getStockCount()<product.getIndividualLimit();
    }
}
