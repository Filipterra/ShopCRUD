package com.shopcrud.alpha.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.shopcrud.alpha.authentication.AccessControlFactory;
import com.shopcrud.alpha.authentication.BasicAccessControl;
import com.shopcrud.alpha.authentication.CurrentUser;
import com.shopcrud.alpha.items.Menu;
import com.shopcrud.alpha.views.AboutView;
import com.shopcrud.alpha.views.ProductView;


@Theme(value = Lumo.class)
@PWA(name = "General store Starter", shortName = "General store")
public class MainLayout extends FlexLayout implements RouterLayout {
	private Menu menu;
	private Button logout;

    public MainLayout() {
        setSizeFull();
        setClassName("main-layout");
        
        logout = new Button("Logout");
        logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        logout.addClickListener(click -> AccessControlFactory.getInstance().createAccessControl().signOut());
        add(logout);
        
        menu = new Menu();
        menu.addView(ProductView.class, ProductView.VIEW_NAME,
                VaadinIcon.EDIT.create());
        menu.addView(OrderView.class, OrderView.VIEW_NAME,
                VaadinIcon.EDIT.create());
        if(BasicAccessControl.isUserAdmin()) {
        	menu.addView(AboutView.class, AboutView.VIEW_NAME, VaadinIcon.INFO_CIRCLE.create());
        	menu.addView(AdminView.class, AdminView.VIEW_NAME, VaadinIcon.DOCTOR.create());
        }
        	Notification.show("Welcome "+CurrentUser.get());
        	
        add(menu);
    }
}
