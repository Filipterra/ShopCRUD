package com.shopcrud.alpha;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.shopcrud.alpha.AboutView;
import com.shopcrud.alpha.SampleCrudView;


@Theme(value = Lumo.class)
@PWA(name = "General store Starter", shortName = "General store")
public class MainLayout extends FlexLayout implements RouterLayout {
	private Menu menu;

    public MainLayout() {
        setSizeFull();
        setClassName("main-layout");

        menu = new Menu();
        menu.addView(SampleCrudView.class, SampleCrudView.VIEW_NAME,
                VaadinIcon.EDIT.create());
        menu.addView(AboutView.class, AboutView.VIEW_NAME,
                VaadinIcon.INFO_CIRCLE.create());
        menu.addView(AdminView.class, AdminView.VIEW_NAME, VaadinIcon.DOCTOR.create());
        add(menu);
    }
}
