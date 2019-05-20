package com.shopcrud.alpha.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import com.shopcrud.alpha.SampleCrudLogic;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.data.ProductDataProvider;
import com.shopcrud.alpha.items.OrderForm;
import com.shopcrud.alpha.items.ProductForm;
import com.shopcrud.alpha.items.ProductGrid;
import com.shopcrud.alpha.objects.Product;

@Route(value = "Inventory", layout = MainLayout.class)
@PageTitle("Inventory")
public class SampleCrudView extends HorizontalLayout
        implements HasUrlParameter<String> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "Inventory";
    private ProductGrid grid;
    private ProductForm form;
    private OrderForm orderForm;
    
    private TextField filter;

    private SampleCrudLogic viewLogic = new SampleCrudLogic(this);
    private Button newProduct;
    private Button newOrder;
    
    private ProductDataProvider dataProvider = new ProductDataProvider();

    public SampleCrudView() {
        setSizeFull();
        HorizontalLayout topLayout = createTopBar();

        grid = new ProductGrid();
        grid.setDataProvider(dataProvider);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        
        form = new ProductForm(viewLogic);
        form.setCategories(DataService.get().getAllCategories());
        
        orderForm = new OrderForm(viewLogic);

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);
        add(orderForm);
        
        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filter name or category");
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));

        newProduct = new Button("New product");
        newProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newProduct.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newProduct.addClickListener(click -> viewLogic.newProduct());
        
        newOrder = new Button("New order");
        newOrder.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newOrder.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newOrder.addClickListener(click -> viewLogic.newOrder());
		
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newProduct);
        topLayout.add(newOrder);
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

    public void setNewProductEnabled(boolean enabled) {
        newProduct.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Product row) {
        grid.getSelectionModel().select(row);
    }

    public Product getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void updateProduct(Product product) {
        dataProvider.save(product);
    }

    public void removeProduct(Product product) {
        dataProvider.delete(product);
    }
    
    public void editProduct(Product product) {
        showForm(product != null);
        form.editProduct(product);
    }
    
    public void showForm(boolean show) {
        form.setVisible(show);
    }
    
    public void editOrder(Product product) {
        showOrderForm(product != null);
        orderForm.editOrder(product);
    }
    
    public void showOrderForm(boolean show) {
        orderForm.setVisible(show);
    }
    
    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
    	
    }
}
