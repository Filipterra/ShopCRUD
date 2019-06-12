package com.shopcrud.alpha.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.shopcrud.alpha.views.MainLayout;
import com.shopcrud.alpha.authentication.BasicAccessControl;
import com.shopcrud.alpha.items.Dashboard;

@Route(value = "About", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends HorizontalLayout {
	public static final String VIEW_NAME = "About";

    public AboutView() {
    	
        add(new Dashboard());

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}
