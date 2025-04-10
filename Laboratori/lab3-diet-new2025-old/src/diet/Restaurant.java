package diet;

import java.util.ArrayList;
import java.util.Arrays;

import diet.Order.OrderStatus;

/**
 * Represents a restaurant class with given opening times and a set of menus.
 */
public class Restaurant {
	
	private String name;
	private ArrayList<String> schedule = new ArrayList<>();
	private ArrayList<Menu> menus = new ArrayList<>();
	private Takeaway takeaway;

    public Restaurant(String name, Takeaway takeaway) {
		this.name = name;
		this.takeaway = takeaway;
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
        
		schedule.addAll(Arrays.asList(hm));
	}

	/**
	 * Checks whether the restaurant is open at the given time.
	 *
	 * @param time time to check
	 * @return {@code true} is the restaurant is open at that time
	 */
	public boolean isOpenAt(String time){
		int i;
		String t1, t2;

		for (i = 0; i < schedule.size(); i += 2) {
			t1 = schedule.get(i);
			t2 = schedule.get(i+1);
			if (time.compareTo(t1) >= 0 && time.compareTo(t2) <= 0)
				return true;
		}

		return false;
	}

	/**
	 * Adds a menu to the list of menus offered by the restaurant
	 *
	 * @param menu	the menu
	 */
	public void addMenu(Menu menu) {
		menus.add(menu);
	}

	/**
	 * Gets the restaurant menu with the given name
	 *
	 * @param name	name of the required menu
	 * @return menu with the given name
	 */
	public Menu getMenu(String name) {
		for (Menu menu : menus)
			if (menu.getName().equals(name))
				return menu;

		return null;
	}

	/**
	 * Retrieve all order with a given status with all the relative details in text format.
	 *
	 * @param status the status to be matched
	 * @return textual representation of orders
	 */
	public String ordersWithStatus(OrderStatus status) {
		ArrayList<Order> os = takeaway.obtainOrders(this.name);
		String s = "";

		for (Order o : os)
			if (o.getStatus().equals(status))
				s += o.toString();

		return s;
	}
}
