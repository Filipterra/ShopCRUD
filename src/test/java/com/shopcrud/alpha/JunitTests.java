package com.shopcrud.alpha;

import org.junit.jupiter.api.Test;

import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.test.logic.DataService;
import com.shopcrud.alpha.test.logic.OrderDataProvider;
import com.shopcrud.alpha.test.logic.ProductDataProvider;
import com.shopcrud.alpha.test.logic.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
 
class JunitTests {
	OrderDataProvider orderDataProvider = new OrderDataProvider();
	ProductDataProvider productDataProvider = new ProductDataProvider();
	
    @Test
    void justAnExample() {
    	assertTrue(true);
    }
    
    @Test
    void Test() {
    	assertEquals(1,1);
    }
    
    
    
    @Test
    void saveProduct() {
    	Product product=new Product();
    	product.setProductName("Test");
    	productDataProvider.save(product);
    	
    	Product found = DataService.get().getProductByName("Test");
    	assertEquals(found.getProductName(), "Test");
    }
    
    @Test
    void findProduct() {
    	Product product=new Product();
    	product.setProductName("Test");
    	productDataProvider.save(product);
    	
    	Product found = DataService.get().getProductByName("Test");
    	assertTrue(found!=null);
    }
    
    @Test
    void deleteProduct() {
    	Product product=new Product();
    	product.setProductName("Test");
    	productDataProvider.save(product);
    	
    	productDataProvider.delete(DataService.get().getProductByName("Test"));
    	
    	Product found = DataService.get().getProductByName("Test");
    	assertTrue(found==null);
    }
    
    @Test
    void saveOrder() {
    	Order order=new Order();
    	order.setProductName("Test");
    	orderDataProvider.save(order);
    	
    	Order found = DataService.get().getOrderById(order.getId());
    	assertEquals(found.getProductName(), "Test");
    }
    
    @Test
    void findOrder() {
    	Order order=new Order();
    	order.setProductName("Test");
    	orderDataProvider.save(order);
    	
    	Order found = DataService.get().getOrderById(order.getId());
    	assertTrue(found!=null);
    }
    
    @Test
    void deleteOrder() {
    	Order order=new Order();
    	order.setProductName("Test");
    	orderDataProvider.save(order);
    	
    	orderDataProvider.delete(DataService.get().getOrderById(order.getId()));
    	
    	Order found = DataService.get().getOrderById(order.getId());
    	assertTrue(found==null);
    }
    
    
}