package diet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;


/**
 * Represents a takeaway restaurant chain.
 * It allows managing restaurants, customers, and orders.
 */
public class Takeaway {

	public static DateFormat dateFormat = new SimpleDateFormat("hh:mm");
	private Food food;
	private ArrayList<Restaurant> restaurants = new ArrayList<>(); 
	private ArrayList<Customer> customers = new ArrayList<>(); 
	private ArrayList<Order> orders = new ArrayList<>();

	/**
	 * Constructor
	 * @param food the reference {@link Food} object with materials and products info.
	 */
	public Takeaway(Food food){
		this.food = food;
	}

	private <T, E extends T> Collection<T> getSortedInfo(ArrayList<E> original, Comparator<E> comparator1, Comparator<E> comparator2) {
		ArrayList<T> s = new ArrayList<>();

		original.sort(comparator1);
		original.sort(comparator2);

		for (E element : original)
			s.add((T) element);
		
		return s;
	}

	/**
	 * Creates a new restaurant with a given name
	 *
	 * @param restaurantName name of the restaurant
	 * @return the new restaurant
	 */
	public Restaurant addRestaurant(String restaurantName) {
		Restaurant r = new Restaurant(restaurantName, this);
		
		restaurants.add(r);

		return r;
	}

	/**
	 * Retrieves the names of all restaurants
	 *
	 * @return collection of restaurant names
	 */
	public Collection<String> restaurants() {
		Collection<String> rs = new ArrayList<>();

		for (Restaurant restaurant : restaurants)
			rs.add(restaurant.getName());
		
		return rs;
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
		
		customers.add(c);

		return c;
	}

	/**
	 * Retrieves all registered customers
	 *
	 * @return sorted collection of customers
	 */
	public Collection<Customer> customers(){
		return getSortedInfo(
			customers, 
			(c1, c2) -> c1.getFirstName().compareTo(c2.getFirstName()),
			(c1, c2) -> c1.getLastName().compareTo(c2.getLastName())
		);
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
		int i;
		boolean found = true;
		Date t1, t2, t;
		Restaurant foundRestaurant = null;
		ArrayList<String> schedule = new ArrayList<>();
		
		for (Restaurant r : restaurants)
			if (r.getName().equals(restaurantName)) {
				schedule = r.getSchedule();
				foundRestaurant = r;
				break;
			}

		if (foundRestaurant != null && !foundRestaurant.isOpenAt(time)) {
			found = false;
			try {
				t = dateFormat.parse(time);
				for (i = 0; i < schedule.size(); i+=2) {
					t1 = dateFormat.parse(schedule.get(i));
					t2 = dateFormat.parse(schedule.get(i+1));
					if (t.after(t1) && t.before(t2)) {
						found = true;
						break;
					}

					if (t.before(t1) && t.before(t2)) {
						String[] times = t1.toString().split(" ")[3].split(":");
						time = times[0] + ":" + times[1];
						found = true;
						break;
					}
				}
			} catch(ParseException error) {
				return null;
			}
		}

		if (!found)
			time = schedule.get(0);

		Order o = new Order(restaurantName, customer, time);
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
		Collection<Restaurant> rs = new ArrayList<>();

		restaurants.sort((r1, r2) -> r1.getName().compareTo(r2.getName()));

		for (Restaurant r : restaurants)
			if (r.isOpenAt(time))
				rs.add(r);

		return rs;
	}

	public ArrayList<Order> obtainOrders(String restaurant) {
		ArrayList<Order> os = new ArrayList<>();

		orders.sort((o1, o2) -> o1.getDeliveryTime().compareTo(o2.getDeliveryTime()));
		orders.sort((o1, o2) -> o1.getUser().compareTo(o2.getUser()));
		orders.sort((o1, o2) -> o1.getRestaurantName().compareTo(o2.getRestaurantName()));
		

		for (Order o : orders)
			if (o.getRestaurantName().equals(restaurant))
				os.add(o);

		return os;
	}
}
