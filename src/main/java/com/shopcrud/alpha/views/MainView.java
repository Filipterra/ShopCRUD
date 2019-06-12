package com.shopcrud.alpha.views;

import com.shopcrud.alpha.authentication.BasicAccessControl;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("Menu")
@PageTitle("Menu")
public class MainView extends HorizontalLayout {

	public MainView() {
		//if(!BasicAccessControl.isUserSignedIn()) getUI().get().navigate("");
		add(new MainLayout());
	}
}
