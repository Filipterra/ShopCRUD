package com.shopcrud.alpha.views;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.shopcrud.alpha.views.MainLayout;
import com.shopcrud.alpha.OrderCrudLogic;
import com.shopcrud.alpha.authentication.BasicAccessControl;
import com.shopcrud.alpha.data.OrderDataProvider;
import com.shopcrud.alpha.items.OrderGrid;
import com.shopcrud.alpha.items.OrderStateForm;
import com.shopcrud.alpha.objects.Order;

@Route(value = "Orders", layout = MainLayout.class)
@PageTitle("Orders")
public class OrderView extends HorizontalLayout
        implements HasUrlParameter<String> {

	public static final String VIEW_NAME = "Orders";
    private OrderGrid grid;
    private OrderStateForm form;
    private TextField filter;

    private OrderCrudLogic viewLogic = new OrderCrudLogic(this);
    
    private OrderDataProvider dataProvider = new OrderDataProvider();

    public OrderView() {
    	
        setSizeFull();
        HorizontalLayout topLayout = createTopBar();

        grid = new OrderGrid();
        if (!BasicAccessControl.isUserAdmin()) dataProvider.setClientFilter();
        grid.setDataProvider(dataProvider);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        form = new OrderStateForm(viewLogic);
        
        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);
        
        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        if (BasicAccessControl.isUserAdmin()) filter.setPlaceholder("Filter name or client");
        else filter.setPlaceholder("Filter product name");
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));
		
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Order row) {
        grid.getSelectionModel().select(row);
    }

    public Order getSelectedRow() {
        return grid.getSelectedRow();
    }
    
    public void editOrder(Order order) {
        showOrderForm(order != null);
        form.editOrder(order);
    }
    
    public void showOrderForm(boolean show) {
        form.setVisible(show);
    }
    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
    	
    }
}
