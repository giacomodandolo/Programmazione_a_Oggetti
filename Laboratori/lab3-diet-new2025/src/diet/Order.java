package diet;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents and order issued by an {@link Customer} for a {@link Restaurant}.
 *
 * When an order is printed to a string is should look like:
 * <pre>
 *  RESTAURANT_NAME, USER_FIRST_NAME USER_LAST_NAME : DELIVERY(HH:MM):
 *  	MENU_NAME_1->MENU_QUANTITY_1
 *  	...
 *  	MENU_NAME_k->MENU_QUANTITY_k
 * </pre>
 */
public class Order {

	private Customer user;
	private String restaurantName;
	private String deliveryTime;
	private OrderStatus orderStatus;
	private PaymentMethod paymentMethod;
	private Map<String, Integer> menus;

	/**
	 * Possible order statuses
	 */
	public enum OrderStatus {
		ORDERED, READY, DELIVERED
	}

	/**
	 * Accepted paymentMethod methods
	 */
	public enum PaymentMethod {
		PAID, CASH, CARD
	}

	public Order(String restaurantName, Customer user, String deliveryTime) {
		this.menus = new HashMap<>();
        this.restaurantName = restaurantName;
        this.user = user;
		this.deliveryTime = deliveryTime;
		this.orderStatus = OrderStatus.ORDERED;
		this.paymentMethod = PaymentMethod.CASH;
    }

	public String getRestaurantName() {
		return restaurantName;
	}

	/**
	 * Set paymentMethod method
	 * @param pm the paymentMethod method
	 */
	public void setPaymentMethod(PaymentMethod pm) {
		paymentMethod = pm;
	}

	/**
	 * Retrieves current paymentMethod method
	 * @return the current method
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Set the new status for the order
	 * @param os new status
	 */
	public void setStatus(OrderStatus os) {
		orderStatus = os;
	}

	/**
	 * Retrieves the current status of the order
	 *
	 * @return current status
	 */
	public OrderStatus getStatus() {
		return orderStatus;
	}

	/**
	 * Add a new menu to the order with a given quantity
	 *
	 * @param menu	menu to be added
	 * @param quantity quantity
	 * @return the order itself (allows method chaining)
	 */
	public Order addMenus(String menu, int quantity) {
		menus.put(menu, quantity);
		
		return this;
	}

    @Override
    public String toString() {
		int i = 0;
		String s = restaurantName + ", " 
			+ user.toString()
			+ " : (";
		
		s += deliveryTime + "):\n\t";

		for (String menu : menus.keySet()) {
			s = s + menu + "->" + menus.get(menu) + "\n";
			if (i < menus.size()-1)
				s = s + "\t";
			i++;
		}

        return s;
    }

	public String getUser() {
		return user.toString();	
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

}
