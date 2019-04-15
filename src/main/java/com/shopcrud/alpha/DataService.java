package com.shopcrud.alpha;

import java.io.Serializable;
import java.util.Collection;
import com.shopcrud.alpha.Category;
import com.shopcrud.alpha.MockDataService;


public abstract class DataService implements Serializable {

    public abstract Collection<Category> getAllCategories();

    public static DataService get() {
        return MockDataService.getInstance();
    }

}
