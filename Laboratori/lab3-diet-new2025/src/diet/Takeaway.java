package diet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;


/**
 * Represents a takeaway restaurant chain.
 * It allows managing restaurants, customers, and orders.
 */
public class Takeaway {

	public static DateFormat dateFormat = new SimpleDateFormat("hh:mm");
	private Food food;
	private Map<String, Restaurant> restaurants; 
	private Map<String, Customer> customers; 
	private ArrayList<Order> orders;

	/**
	 * Constructor
	 * @param food the reference {@link Food} object with materials and products info.
	 */
	public Takeaway(Food food){
		this.restaurants = new TreeMap<>();
		this.customers = new TreeMap<>();
		this.orders = new ArrayList<>();
		this.food = food;
	}

	/**
	 * Creates a new restaurant with a given name
	 *
	 * @param restaurantName name of the restaurant
	 * @return the new restaurant
	 */
	public Restaurant addRestaurant(String restaurantName) {
		Restaurant r = new Restaurant(restaurantName);
		
		restaurants.put(restaurantName, r);

		return r;
	}

	/**
	 * Retrieves the names of all restaurants
	 *
	 * @return collection of restaurant names
	 */
	public Collection<String> restaurants() {
		return new ArrayList<>(restaurants.keySet());
	}

	/**
	 * Creates a new customer for the takeaway
	 * @param firstName first name of the customer
	 * @param lastName	last name of the customer
	 * @param email		email of the customer
	 * @param phoneNumber mobile phone number
	 *
	 * @return the object representing the newly created customer
	 */
	public Customer registerCustomer(String firstName, String lastName, String email, String phoneNumber) {
		Customer c = new Customer(lastName, firstName, email, phoneNumber);
		
		customers.put(lastName + firstName, c);

		return c;
	}

	/**
	 * Retrieves all registered customers
	 *
	 * @return sorted collection of customers
	 */
	public Collection<Customer> customers() {
		return new ArrayList<>(customers.values());
	}

	/**
	 * Creates a new order for the chain.
	 *
	 * @param customer		 customer issuing the order
	 * @param restaurantName name of the restaurant that will take the order
	 * @param time	time of desired delivery
	 * @return order object
	 */
	public Order createOrder(Customer customer, String restaurantName, String time) {
		Order o;
		Restaurant r = restaurants.get(restaurantName);

		if (!r.isOpenAt(time))
			time = r.getNextTime(time);

		
		if (time.length() < 5)
			time = "0" + time;

		o = new Order(restaurantName, customer, time);
		r.addOrder(o);
		orders.add(o);

		return o;
	}

	/**
	 * Find all restaurants that are open at a given time.
	 *
	 * @param time the time with format {@code "HH:MM"}
	 * @return the sorted collection of restaurants
	 */
	public Collection<Restaurant> openRestaurants(String time){
		ArrayList<Restaurant> rs = new ArrayList<>();

		for (Restaurant r : restaurants.values())
			if (r.isOpenAt(time))
				rs.add(r);
		
		return rs;
	}
}
