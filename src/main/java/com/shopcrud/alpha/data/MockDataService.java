package com.shopcrud.alpha.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Product;
import com.vaadin.flow.component.notification.Notification;

public class MockDataService extends DataService {

    private static MockDataService INSTANCE;

    private List<Product> products;
    private List<Category> categories;
    private int nextProductId = 0;
    private int nextCategoryId = 0;

    private MockDataService() {
        categories = MockDataGenerator.createCategories();
        products = MockDataGenerator.createProducts(categories);
        nextProductId = products.size() + 1;
        nextCategoryId = categories.size() + 1;
    }

    public synchronized static DataService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDataService();
        }
        return INSTANCE;
    }

    @Override
    public synchronized List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public synchronized List<Category> getAllCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public synchronized void updateProduct(Product p) {
        if (p.getId() < 0) {
            p.setId(nextProductId++);
            products.add(p);
            return;
        }
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == p.getId()) {
                products.set(i, p);
                return;
            }
        }

        throw new IllegalArgumentException("No product with id " + p.getId()
                + " found");
    }
    
    
    public synchronized void updateAvailability() {
    	for (Iterator<Product> i = products.iterator(); i.hasNext();) {
    	    i.next().setAvailability();
    	}
    }

    @Override
    public synchronized Product getProductById(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                return products.get(i);
            }
        }
        return null;
    }

    public synchronized Product getProductByName(String productName) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductName().equals(productName)) {
                return products.get(i);
            }
        }
        return null;
    }
    
    @Override
    public void updateCategory(Category category) {
        if (category.getId() < 0) {
            category.setId(nextCategoryId++);
            categories.add(category);
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        if (categories.removeIf(category -> category.getId() == categoryId)) {
            getAllProducts().forEach(product -> {
                product.getCategory().removeIf(category -> category.getId() == categoryId);
            });
        }
    }

    @Override
    public synchronized void deleteProduct(int productId) {
        Product p = getProductById(productId);
        if (p == null) {
            throw new IllegalArgumentException("Product with id " + productId
                    + " not found");
        }
        products.remove(p);
    }

}
