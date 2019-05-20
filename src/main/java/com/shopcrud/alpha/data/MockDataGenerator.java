package com.shopcrud.alpha.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Availability;
import com.shopcrud.alpha.objects.Product;

public class MockDataGenerator {
    private static int nextCategoryId = 1;
    private static int nextProductId = 1;
    private static final Random random = new Random(1);
    private static final String categoryNames[] = new String[] {
            "Books", "Foodstuffs", "Medicine", "Stationery"};
    private static final String productNames[] = new String[] {
            "Silmarilion", "Dr Pepper", "Aspirine", "Pencil"};

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

        p.setPrice(new BigDecimal((random.nextInt(250) + 50) / 10.0));
        p.setStockCount(random.nextInt(23));
        p.setAvailability();

        HashSet<Category> productCategories = new HashSet<Category>();
        productCategories.add(categories.get(random.nextInt(categories
                .size())));
        p.setCategory(productCategories);
        return p;
    }

}
