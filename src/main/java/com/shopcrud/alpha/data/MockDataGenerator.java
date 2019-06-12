package com.shopcrud.alpha.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.objects.Status;
import com.shopcrud.alpha.objects.Product;

public class MockDataGenerator {
    private static int nextCategoryId = 1;
    private static int nextProductId = 1;
    private static int nextOrderId = 1;
    private static final Random random = new Random(1);
    private static final String categoryNames[] = new String[] {
            "Books", "Foodstuffs", "Medicine", "Stationery"};
    private static final String productNames[] = new String[] {
            "Silmarilion", "Dr Pepper", "Aspirine", "Pencil"};
    private static final String clientNames[] = new String[] {
            "aNowak", "pKowalski", "mCesarz", "kFelczak"};

    static List<Category> createCategories() {
        List<Category> categories = new ArrayList<Category>();
        for (String name : categoryNames) {
            Category c = createCategory(name);
            categories.add(c);
        }
        return categories;
    }
    
    static List<Product> createProducts(List<Category> categories) {
        List<Product> products = new ArrayList<Product>();
        for (String name : productNames) {
            Product p = createProduct(name, categories);
            products.add(p);
        }
        return products;
    }
    
    static List<Order> createOrders(List<Product> products) {
    	List<Order> orders = new ArrayList<Order>();
        for (String name : clientNames) {
            Order o = createOrder(name, products);
            orders.add(o);
            o = createOrder(name, products);
            orders.add(o);
        }
        return orders;
	}

    private static Order createOrder(String name, List<Product> products) {
    	Order o = new Order();
        o.setId(nextOrderId++);
        o.setClient(name);
        if(nextOrderId<5) o.setStatus(Status.ACCEPTED);
        else o.setStatus(Status.PLACED);
        int h=random.nextInt(18)%products.size();
        o.setProductName(products.get(h).getProductName());
        o.setAmount(random.nextInt(8));
        o.setPrice(new BigDecimal(o.getAmount()).multiply(products.get(h).getPrice()));
        
        return o;
	}

	private static Category createCategory(String name) {
        Category c = new Category();
        c.setId(nextCategoryId++);
        c.setName(name);
        return c;
    }

    private static Product createProduct(String name, List<Category> categories) {
        Product p = new Product();
        p.setId(nextProductId++);
        p.setProductName(name);

        p.setPrice(new BigDecimal((random.nextInt(150) + 50) / 10.0));
        p.setStockCount(random.nextInt(23));
        p.setAvailability();

        HashSet<Category> productCategories = new HashSet<Category>();
        productCategories.add(categories.get(nextProductId-2));
        p.setCategory(productCategories);
        return p;
    }

}
