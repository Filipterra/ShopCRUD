package com.shopcrud.alpha;

import java.util.ArrayList;
import java.util.List;
import com.shopcrud.alpha.Category;

public class MockDataGenerator {
    private static int nextCategoryId = 1;
    private static final String categoryNames[] = new String[] {
            "Books", "Foodstuffs", "Medicine", "Stationery"};

    static List<Category> createCategories() {
        List<Category> categories = new ArrayList<Category>();
        for (String name : categoryNames) {
            Category c = createCategory(name);
            categories.add(c);
        }
        return categories;

    }

    private static Category createCategory(String name) {
        Category c = new Category();
        c.setId(nextCategoryId++);
        c.setName(name);
        return c;
    }

}
