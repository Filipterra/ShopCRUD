package com.shopcrud.alpha.test.logic;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.shopcrud.alpha.test.logic.DataService;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.test.logic.Product;
import com.shopcrud.alpha.objects.Status;

public class MockDataService extends DataService {

    private static MockDataService INSTANCE;

    private List<Product> products;
    private List<Order> orders;
    private List<Category> categories;
    private int nextProductId = 0;
    private int nextOrderId = 0;
    private int nextCategoryId = 0;

    private MockDataService() {
        categories = MockDataGenerator.createCategories();
        products = MockDataGenerator.createProducts(categories);
        orders = MockDataGenerator.createOrders(products);
        nextProductId = products.size() + 1;
        nextOrderId = orders.size() + 1;
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
	public synchronized List<Order> getAllOrders() {
		return Collections.unmodifiableList(orders);
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
    
    @Override
	public synchronized void updateOrder(Order o) {
    	if (o.getId() < 0) {
            o.setId(nextOrderId++);
            o.setStatus(Status.PLACED);
            orders.add(o);
            return;
        }
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId() == o.getId()) {
                orders.set(i, o);
                return;
            }
        }

        throw new IllegalArgumentException("No order with id " + o.getId()
                + " found");
	}
    
    @Override
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

    @Override
    public synchronized Product getProductByName(String productName) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductName().equals(productName)) {
                return products.get(i);
            }
        }
        return null;
    }
    
	@Override
	public synchronized Order getOrderById(int orderId) {
		for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId() == orderId) {
                return orders.get(i);
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

	@Override
	public synchronized void deleteOrder(int orderId) {
		Order o = getOrderById(orderId);
        if (o == null) {
            throw new IllegalArgumentException("Order with id " + orderId
                    + " not found");
        }
        orders.remove(o);
	}

}
