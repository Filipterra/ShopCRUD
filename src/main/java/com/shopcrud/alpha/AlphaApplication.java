package com.shopcrud.alpha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;


@SpringBootApplication
public class AlphaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlphaApplication.class, args);
	}
	
	@Route
	public static class MainView extends HorizontalLayout {

		private static final long serialVersionUID = 1L;

		public MainView() {
			add(new MainLayout());
		}
	}
	
}
