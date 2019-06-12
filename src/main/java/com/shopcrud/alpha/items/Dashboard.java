package com.shopcrud.alpha.items;

import java.math.BigDecimal;
import java.util.List;

import com.shopcrud.alpha.data.DataService;
import com.shopcrud.alpha.objects.Category;
import com.shopcrud.alpha.objects.Order;
import com.shopcrud.alpha.objects.Status;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Dashboard extends VerticalLayout {
		private Board board;

		
    public Dashboard() {
    	board = new Board();
    	
    	Chart chart1=new Chart(ChartType.PIE);
    	Chart chart2=new Chart(ChartType.BAR);
    	
    	PieConfigure(chart1.getConfiguration());
    	BarConfigure(chart2.getConfiguration());
    	
    	board.add(chart1, chart2);
    	add(board);
    }
    
    public void BarConfigure(Configuration conf)
    {
    	List<Category> categories =(List<Category>) DataService.get().getAllCategories();
    	conf.getLegend().setEnabled(false);
    	conf.setTitle("Total ordered");
    	ListSeries series = new ListSeries("All");
    	
    	XAxis xaxis = new XAxis();
    	for (Category category : categories) {
    		xaxis.addCategory(category.getName());
    		series.addData(sumAmountAllOrders(category));
    	}
    	conf.addSeries(series);
    	xaxis.setTitle("Categories");
    	conf.addxAxis(xaxis);
    	YAxis yaxis1 = new YAxis();
    	
    	yaxis1.setTitle("Sales");
    	conf.addyAxis(yaxis1);
    }
    
    public void PieConfigure(Configuration conf)
    {
    	List<Category> categories =(List<Category>) DataService.get().getAllCategories();
    	
    	conf.setTitle("Total income");
    	DataSeries series = new DataSeries("All");
    	
    	for (Category category : categories) {
    		series.add(new DataSeriesItem(category.getName(), sumPriceAcceptedOrders(category)));
    	}
    	conf.addSeries(series);
    }
    
    private int sumAmountAllOrders(Category category) {
    	List<Order> orders =(List<Order>) DataService.get().getAllOrders();
    	int sum=0;
    	for (Order order : orders) {
    		if(DataService.get().getProductByName(order.getProductName()).getCategory().contains(category)) {
    			sum+=order.getAmount();
    		}
    	}
    	return sum;
    }
    
    private BigDecimal sumPriceAcceptedOrders(Category category) {
    	List<Order> orders =(List<Order>) DataService.get().getAllOrders();
    	BigDecimal sum = new BigDecimal(0);
    	for (Order order : orders) {
    		if (order.getStatus().equals(Status.ACCEPTED)
    				&& DataService.get().getProductByName(order.getProductName()).getCategory().contains(category)) {
    			sum=sum.add(order.getPrice());
    		}
    	}
    	return sum;
    }
}