package com.shopcrud.alpha.views;

import java.util.ArrayList;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.ironlist.IronList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.shopcrud.alpha.authentication.BasicAccessControl;
import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.data.ProductDataProvider;
import com.shopcrud.alpha.items.TresholdGrid;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Product;

@Route(value = "Admin", layout = MainLayout.class)
@PageTitle("Admin")
public class AdminView extends HorizontalLayout {
	

	public static final String VIEW_NAME = "Admin";

    private final IronList<Category> categoriesListing;
    private final ListDataProvider<Category> dataProvider;
    private final Button newCategoryButton;
    private final TextField newLimit;
    private final Button saveNewLimit;
    private TresholdGrid grid;
    private ProductDataProvider prDataProvider = new ProductDataProvider();

    public AdminView() {
    	
    	VerticalLayout Vcategories = new VerticalLayout();
        categoriesListing = new IronList<>();

        dataProvider = new ListDataProvider<Category>(new ArrayList<>(DataService.get().getAllCategories())) {};
        categoriesListing.setDataProvider(dataProvider);
        categoriesListing.setRenderer(new ComponentRenderer<>(this::createCategoryEditor));

        newCategoryButton = new Button("Add New Category", event -> {
            Category category = new Category();
            dataProvider.getItems().add(category);
            dataProvider.refreshAll();
        });
        newCategoryButton.setDisableOnClick(true);

        
        Vcategories.add(new H2("Admin Configuration"), new H4("Edit Categories"), newCategoryButton, categoriesListing);
        Vcategories.setWidthFull();
        Vcategories.setMaxWidth("350px");
        add(Vcategories);
        
        VerticalLayout VsetLimit = new VerticalLayout();
        
        newLimit=new TextField("\"Last items threshold\"");
        newLimit.setPlaceholder(Product.getLimit().toString());
        newLimit.setWidth("100%");
        newLimit.setValueChangeMode(ValueChangeMode.EAGER);
        VsetLimit.add(newLimit);
        
        saveNewLimit = new Button("Save");
        saveNewLimit.setWidth("100%");
        saveNewLimit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveNewLimit.addClickListener(event -> {
        	Product.setLimit(Integer.parseInt(newLimit.getValue()));
        	Notification.show("\"Last items threshold\" changed to "+Product.getLimit());
        	});
        VsetLimit.add(saveNewLimit);
        VsetLimit.setMaxWidth("250px");
        add(VsetLimit);
        
        VerticalLayout Vgrid = new VerticalLayout();
        
        Label gridLabel = new Label("Products below set treshold");
        
        grid = new TresholdGrid();
        prDataProvider.setLimitFilter();
        grid.setDataProvider(prDataProvider);
        
        Vgrid.add(gridLabel, grid);
        Vgrid.setSizeFull();
        Vgrid.setFlexGrow(1, grid);
        add(Vgrid);
        
        this.setSizeFull();
    };

    private Component createCategoryEditor(Category category) {
        TextField nameField = new TextField();
        if (category.getId() < 0) {
            nameField.focus();
        }
        
        Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create(),event -> {
            DataService.get().deleteCategory(category.getId());
            dataProvider.getItems().remove(category);
            dataProvider.refreshAll();
            Notification.show("Category Deleted.");
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        BeanValidationBinder<Category> binder = new BeanValidationBinder<>(Category.class);
        binder.forField(nameField).bind("name");
        binder.setBean(category);
        binder.addValueChangeListener(event -> {
            if (binder.isValid()) {
                DataService.get().updateCategory(category);
                deleteButton.setEnabled(true);
                newCategoryButton.setEnabled(true);
                Notification.show("Category Saved.");
            }
        });
        deleteButton.setEnabled(category.getId() > 0);

        HorizontalLayout layout = new HorizontalLayout(nameField, deleteButton);
        layout.setFlexGrow(1);
        return layout;
    }

}
