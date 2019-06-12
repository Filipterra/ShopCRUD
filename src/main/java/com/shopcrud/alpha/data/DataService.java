package com.shopcrud.alpha.data;

import java.io.Serializable;
import java.util.Collection;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.objects.Product;
import com.shopcrud.alpha.data.MockDataService;


public abstract class DataService implements Serializable {

	public abstract Collection<Product> getAllProducts();
	
	public abstract Collection<Order> getAllOrders();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateProduct(Product p);
    
    public abstract void updateOrder(Order o);

    public abstract void deleteProduct(int productId);
    
    public abstract void deleteOrder(int orderId);

    public abstract Product getProductById(int productId);
    
    public abstract Order getOrderById(int orderId);
    
    public abstract Product getProductByName(String productName);

    public abstract void updateCategory(Category category);

    public abstract void deleteCategory(int categoryId);
    
    public abstract void updateAvailability();
    
    public static DataService get() {
        return MockDataService.getInstance();
    }

}
