package com.shopcrud.alpha.data;

import java.io.Serializable;
import java.util.Collection;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Product;
import com.shopcrud.alpha.data.MockDataService;


public abstract class DataService implements Serializable {

	public abstract Collection<Product> getAllProducts();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateProduct(Product p);

    public abstract void deleteProduct(int productId);

    public abstract Product getProductById(int productId);
    
    public abstract Product getProductByName(String productName);

    public abstract void updateCategory(Category category);

    public abstract void deleteCategory(int categoryId);
    
    public abstract void updateAvailability();
    
    public static DataService get() {
        return MockDataService.getInstance();
    }

}
