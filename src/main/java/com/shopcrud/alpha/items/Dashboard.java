package com.shopcrud.alpha.items;

import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Dashboard extends VerticalLayout {
		private Board board;
		
    public Dashboard() {
    	board = new Board();
    	
    	Chart chart1=new Chart(ChartType.COLUMN);
    	Chart chart2=new Chart(ChartType.PIE);
    	Chart chart3=new Chart(ChartType.BAR);
    	
    	PlaceholderConfigure(chart1.getConfiguration());
    	PlaceholderConfigure(chart2.getConfiguration());
    	PlaceholderConfigure(chart3.getConfiguration());
    	
    	board.add(chart1, chart2, chart3);
    	add(board);
    }
    
    public void PlaceholderConfigure(Configuration conf)
    {
    	conf.setTitle("PlaceholderSales");
    	ListSeries series = new ListSeries("Books");
    	series.setData(4900,  12100,  12800,
    	               6800,  143000, 125000,
    	               51100, 49500);
    	conf.addSeries(series);
    	XAxis xaxis = new XAxis();
    	xaxis.setCategories("Jan", "Feb",   "Mar",
    	                    "Apr",    "May", "Jun",
    	                    "Jul",  "Aug");
    	xaxis.setTitle("Months");
    	conf.addxAxis(xaxis);
    	YAxis yaxis1 = new YAxis();
    	yaxis1.setTitle("Sales");
    	conf.addyAxis(yaxis1);
    }
    
    
}