package com.shopcrud.alpha;

import com.vaadin.flow.component.UI;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.objects.Product;
import com.shopcrud.alpha.objects.Status;
import com.shopcrud.alpha.views.OrderView;

import java.io.Serializable;
public class OrderCrudLogic implements Serializable {

    private OrderView view;

    public OrderCrudLogic(OrderView simpleCrudView) {
        view = simpleCrudView;
    }

    public void init() {
        editOrder(null);
    }

    public void cancelOrder() {
        setFragmentParameter("");
        view.clearSelection();
    }

    private void setFragmentParameter(String orderId) {
        String fragmentParameter;
        if (orderId == null || orderId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = orderId;
        }

        UI.getCurrent().navigate(OrderView.class, fragmentParameter);
    }

    public void editOrder(Order order) {
    	
        if (order == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(order.getId() + "");
        }
        view.editOrder(order);
        
    }
    
    public void acceptOrder(Order order) {
    	Product tmpProduct=DataService.get().getProductByName(order.getProductName());
    	if (tmpProduct!=null)	{
    	tmpProduct.setStockCount(tmpProduct.getStockCount()-order.getAmount());
    	tmpProduct.setAvailability();
    	order.setStatus(Status.ACCEPTED);
    	}
    	view.clearSelection();
    	setFragmentParameter("");
    }
    
    public void cancelOrder(Order order) {
    	order.setStatus(Status.CANCELLED);
    	view.clearSelection();
    	setFragmentParameter("");
    }
    
    public void rowSelected(Order order) {
            editOrder(order);
    }
}
