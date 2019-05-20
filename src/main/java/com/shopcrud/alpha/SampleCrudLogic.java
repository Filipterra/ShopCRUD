package com.shopcrud.alpha;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Product;
import com.shopcrud.alpha.views.SampleCrudView;

import java.io.Serializable;

public class SampleCrudLogic implements Serializable {

    private SampleCrudView view;

    public SampleCrudLogic(SampleCrudView simpleCrudView) {
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

        UI.getCurrent().navigate(SampleCrudView.class, fragmentParameter);
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
        view.clearSelection();
        product.setAvailability();
        view.updateProduct(product);
        setFragmentParameter("");
        view.showSaveNotification(product.getProductName()
                + (newProduct ? " created" : " updated"));
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
    
    public void editOrder(Product product) {
    	
        if (product == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(product.getId() + "");
        }
        view.editOrder(product);
        
    }
    
    public void newOrder() {
    	
        view.clearSelection();
        view.editOrder(new Product());
        
    }
    
    public void saveOrder(Product product) {
    	
        view.clearSelection();
        //product.setAvailability();
        view.updateProduct(product);
        setFragmentParameter("");
        Notification.show("Order Placed");
         
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
