package com.shopcrud.alpha;

import java.util.ArrayList;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.ironlist.IronList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.shopcrud.alpha.DataService;
import com.shopcrud.alpha.Category;


@Route(value = "Admin", layout = MainLayout.class)
@PageTitle("Admin")
public class AdminView extends VerticalLayout {

	public static final String VIEW_NAME = "Admin";

    private final IronList<Category> categoriesListing;
    private final ListDataProvider<Category> dataProvider;
    private final Button newCategoryButton;

    public AdminView() {
        categoriesListing = new IronList<>();

        dataProvider = new ListDataProvider<Category>(new ArrayList<>(DataService.get().getAllCategories())) {};
        categoriesListing.setDataProvider(dataProvider);
        categoriesListing.setRenderer(new ComponentRenderer<>(this::createCategoryEditor));

        newCategoryButton = new Button("Add New Category");

        add(new H2("Admin Configuration"), new H4("Edit Categories"), newCategoryButton, categoriesListing);
    };

    private Component createCategoryEditor(Category category) {
        TextField nameField = new TextField();
        
		
        Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        BeanValidationBinder<Category> binder = new BeanValidationBinder<>(Category.class);
        binder.forField(nameField).bind("name");
        binder.setBean(category);
        deleteButton.setEnabled(category.getId() > 0);

        HorizontalLayout layout = new HorizontalLayout(nameField, deleteButton);
        layout.setFlexGrow(1);
        return layout;
    }

}
