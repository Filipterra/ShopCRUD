package com.shopcrud.alpha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.shopcrud.alpha.authentication.AccessControl;
import com.shopcrud.alpha.authentication.AccessControlFactory;
import com.shopcrud.alpha.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@SpringBootApplication
public class AlphaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlphaApplication.class, args);
	}
	
	@Route("")
	@PageTitle("")
	public static class LoginScreen extends FlexLayout {

	    static private AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

	    public LoginScreen() {
	        //accessControl = AccessControlFactory.getInstance().createAccessControl();
	        buildUI();
	    }

	    private void buildUI() {
	        setSizeFull();
	        setClassName("login-screen");

	        LoginForm loginForm = new LoginForm();
	        loginForm.addLoginListener(this::login);
	        loginForm.addForgotPasswordListener(
	                event -> Notification.show("Hint: same as username"));

	        FlexLayout centeringLayout = new FlexLayout();
	        centeringLayout.setSizeFull();
	        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
	        centeringLayout.setAlignItems(Alignment.CENTER);
	        centeringLayout.add(loginForm);

	        Component loginInformation = buildLoginInformation();

	        add(loginInformation);
	        add(centeringLayout);
	    }

	    private Component buildLoginInformation() {
	        VerticalLayout loginInformation = new VerticalLayout();
	        loginInformation.setClassName("login-information");

	        H1 loginInfoHeader = new H1("Login Information");
	        Span loginInfoText = new Span(
	                "Log in as \"admin\" to have full access. Log in with any " +
	                        "other username to have read-only access. For all " +
	                        "users, the password is same as the username.");
	        loginInformation.add(loginInfoHeader);
	        loginInformation.add(loginInfoText);

	        return loginInformation;
	    }

	    private void login(LoginForm.LoginEvent event) {
	        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
	            getUI().get().navigate("Menu");
	        } else {
	            event.getSource().setError(true);
	        }
	    }
	    
	    public static boolean isUserAdmin() {
	    	if (accessControl==null) return false;
	    	else if (accessControl.isUserInRole("admin")) return true;
	    	return false;
	    }
	}
	
}
