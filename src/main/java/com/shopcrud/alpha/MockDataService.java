package com.shopcrud.alpha;

import java.util.Collections;
import java.util.List;

import com.shopcrud.alpha.DataService;
import com.shopcrud.alpha.Category;

public class MockDataService extends DataService {

    private static MockDataService INSTANCE;

    private List<Category> categories;

    private MockDataService() {
        categories = MockDataGenerator.createCategories();
    }

    public synchronized static DataService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDataService();
        }
        return INSTANCE;
    }

    @Override
    public synchronized List<Category> getAllCategories() {
        return Collections.unmodifiableList(categories);
    }

}
