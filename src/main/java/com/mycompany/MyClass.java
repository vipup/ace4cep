package com.mycompany;

import java.util.Collection;
import java.util.Random;

public class MyClass {
	public static double percent(double fulfilled, double total) {
		double retval = 0;
		try {retval = fulfilled/total*100.00000001;}catch(Throwable e) {			
		}
		return retval;
	}
	public static double percent(MyEvent e) {
		return percent(e.getFulfilled(), e.getTotal());
	}
	public static double myFunction(double price, double volume) {
		return price*volume;
	}
	
	public static double doCompute(EventBean eventBean) {
		return new Random().nextDouble();
}
	public static double doCompute(MyEvent eventBean) {
		return eventBean.getValue();
}
	public static boolean doCheck(MyEvent myEvent, String text) {
		return new Random().nextDouble() > 0.5;
	}
	public static String doSearch(Collection<EventBean> events) {
		return "hereIsOnlyRandom"+ new Random().nextInt();
	}	

}
