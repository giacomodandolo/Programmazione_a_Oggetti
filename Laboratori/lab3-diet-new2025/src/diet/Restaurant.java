package diet;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import diet.Order.OrderStatus;

/**
 * Represents a restaurant class with given opening times and a set of menus.
 */
public class Restaurant {
	
	private String name;
	private Map<String, Menu> menus;
	private ArrayList<String> schedule;
	private Collection<Order> orders;
	
    public Restaurant(String name) {
		this.menus = new HashMap<>();
		this.schedule = new ArrayList<>();
		this.orders = new ArrayList<>();
		this.name = name;
    }

	/**
	 * retrieves the name of the restaurant.
	 *
	 * @return name of the restaurant
	 */
	public String getName() {
		return name;
	}

	public ArrayList<String> getSchedule() {
		return schedule;
	}

	/**
	 * Define opening times.
	 * Accepts an array of strings (even number of elements) in the format {@code "HH:MM"},
	 * so that the closing hours follow the opening hours
	 * (e.g., for a restaurant opened from 8:15 until 14:00 and from 19:00 until 00:00,
	 * arguments would be {@code "08:15", "14:00", "19:00", "00:00"}).
	 *
	 * @param hm sequence of opening and closing times
	 */
	public void setHours(String ... hm) {
		if (hm.length%2 != 0)
			return;
        
		for (String h : hm) {
			if (h.length() < 5)
				h = "0" + h;
			schedule.add(h);
		}
	}

	private boolean compareDatesBefore(String t1, String t2) {
		LocalTime t1Date, t2Date;

		t1Date = LocalTime.parse(t1);
		t2Date = LocalTime.parse(t2);

		return t1Date.isBefore(t2Date);
	}

	private boolean betweenDates(String t1, String t2, String time) {
		LocalTime t1Date, t2Date, timeDate;

		t1Date = LocalTime.parse(t1);
		t2Date = LocalTime.parse(t2);
		timeDate = LocalTime.parse(time);

		return (t1Date.isBefore(timeDate) && t2Date.isAfter(timeDate)) 
			|| (t1Date.equals(timeDate) || t2Date.equals(timeDate));
	}

	/**
	 * Checks whether the restaurant is open at the given time.
	 *
	 * @param time time to check
	 * @return {@code true} is the restaurant is open at that time
	 */
	public boolean isOpenAt(String time) {
		int i;

		if (time.length() < 5)
			time = "0" + time;

		for (i = 0; i < schedule.size(); i += 2)
			if (betweenDates(schedule.get(i), schedule.get(i+1), time))
				return true;

		return false;
	}

	/**
	 * Adds a menu to the list of menus offered by the restaurant
	 *
	 * @param menu	the menu
	 */
	public void addMenu(Menu menu) {
		menus.put(menu.getName(), menu);
	}

	/**
	 * Gets the restaurant menu with the given name
	 *
	 * @param name	name of the required menu
	 * @return menu with the given name
	 */
	public Menu getMenu(String name) {
		return menus.get(name);
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	/**
	 * Retrieve all order with a given status with all the relative details in text format.
	 *
	 * @param status the status to be matched
	 * @return textual representation of orders
	 */
	public String ordersWithStatus(OrderStatus status) {
		String s = "";
		ArrayList<Order> os = new ArrayList<>(orders);
		
		os.sort(
			(o1, o2) -> 
				(o1.getRestaurantName() + ", " + o1.getUser() + ", " + o1.getDeliveryTime())
				.compareTo(
					(o2.getRestaurantName() + ", " + o2.getUser() + ", " + o2.getDeliveryTime())
				)
		);
		
		for (Order o : os)
			if(o.getStatus().equals(status))
				s += o.toString();


		return s;
	}

    public String getNextTime(String time) {
		int i;
		String t = schedule.get(0);

		if (time.length() < 5)
			time = "0" + time;

		for (i = 2; i < schedule.size(); i+=2)
			if (compareDatesBefore(time, schedule.get(i)))
				t = schedule.get(i);
		
		return t;
	}
}
