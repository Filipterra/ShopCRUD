package com.shopcrud.alpha;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.objects.Product;
import com.shopcrud.alpha.views.ProductView;

import java.io.Serializable;

public class ProductCrudLogic implements Serializable {

    private ProductView view;

    public ProductCrudLogic(ProductView simpleCrudView) {
        view = simpleCrudView;
    }

    public void init() {
        editProduct(null);
        editOrder(null);
    }

    public void cancelProduct() {
        setFragmentParameter("");
        view.clearSelection();
        editProduct(null);
    }

    private void setFragmentParameter(String productId) {
        String fragmentParameter;
        if (productId == null || productId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = productId;
        }

        UI.getCurrent().navigate(ProductView.class, fragmentParameter);
    }

    public void enter(String productId) {
        if (productId != null && !productId.isEmpty()) {
            if (productId.equals("new")) {
                newProduct();
            } else {
                try {
                    int pid = Integer.parseInt(productId);
                    Product product = findProduct(pid);
                    view.selectRow(product);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Product findProduct(int productId) {
        return DataService.get().getProductById(productId);
    }

    public void saveProduct(Product product) {
        boolean newProduct = product.isNewProduct();
        if (newProduct && DataService.get().getProductByName(product.getProductName())!=null)
        {
        	Notification.show("Product with this name already exists.");
        }
        else {
	        view.clearSelection();
	        product.setAvailability();
	        view.updateProduct(product);
	        setFragmentParameter("");
	        view.showSaveNotification(product.getProductName()
	                + (newProduct ? " created" : " updated"));	
        }
    }

    public void deleteProduct(Product product) {
        view.clearSelection();
        view.removeProduct(product);
        setFragmentParameter("");
        view.showSaveNotification(product.getProductName() + " removed");
    }

    public void editProduct(Product product) {
        if (product == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(product.getId() + "");
        }
        view.editProduct(product);
    }

    public void newProduct() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProduct(new Product());
    }
    
    public void editOrder(Order order) {
    	
        if (order == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(order.getId() + "");
        }
        view.editOrder(order);
        
    }
    
    public void newOrder() {	
        view.clearSelection();
        view.editOrder(new Order()); 
    }
    
    public void saveOrder(Order order) {
        view.clearSelection();
        view.updateOrder(order);
        setFragmentParameter("");
        Notification.show("Order Placed");
        view.editOrder(null);
    }
    
    public void cancelOrder() {
        setFragmentParameter("");
        view.clearSelection();
        view.editOrder(null);   
    }

    public void rowSelected(Product product) {
            editProduct(product);
    }
}
