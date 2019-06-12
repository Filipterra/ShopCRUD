package com.shopcrud.alpha.authentication;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public class BasicAccessControl implements AccessControl {
	
    @Override
    public boolean signIn(String username, String password) {
        if (username == null || username.isEmpty())
            return false;

        if (!username.equals(password))
            return false;

        CurrentUser.set(username);
        return true;
    }

    public static boolean isUserSignedIn() {
        return !CurrentUser.get().equals("");
    }

    @Override
    public boolean isUserInRole(String role) {
        if ("admin".equals(role)) {
            return getPrincipalName().equals("admin");
        }

        return true;
    }
    
    public static boolean isUserAdmin() {
    	return CurrentUser.get().equals("admin");
    }
    
    @Override
    public String getPrincipalName() {
        return CurrentUser.get();
    }

    @Override
    public void signOut() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().navigate("");
        //UI.getCurrent().getPage().reload();
    }
}
