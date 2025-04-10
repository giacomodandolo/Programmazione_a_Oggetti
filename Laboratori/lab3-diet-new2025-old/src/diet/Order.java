package diet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import diet.Order.OrderStatus;

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
	private ArrayList<OrderMenu> orderMenus = new ArrayList<>();

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
        this.restaurantName = restaurantName;
        this.user = user;
		this.deliveryTime = deliveryTime;
		this.orderStatus = OrderStatus.ORDERED;
		this.paymentMethod = PaymentMethod.CASH;
    }

	private class OrderMenu {
		private String menu;
		private int quantity;

		private OrderMenu(String menu, int quantity) {
			this.menu = menu;
			this.quantity = quantity;
		}

		public String getMenu() {
			return menu;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

        @Override
        public String toString() {
            return menu + "->" + quantity + "\n";
        }
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
		OrderMenu om = new OrderMenu(menu, quantity);
		
		for (OrderMenu o : orderMenus)
			if (o.getMenu().equals(menu)) {
				o.setQuantity(quantity);
				return this;
			}
		
		orderMenus.add(om);

		return this;
	}

    @Override
    public String toString() {
		int i = 0;
		String s = restaurantName + ", " 
			+ user.toString()
			+ " : (";

		if (deliveryTime.length() < 5)
			s += "0";
		
		s += deliveryTime + "):\n\t";

		orderMenus.sort((om1, om2) -> om1.getMenu().compareTo(om2.getMenu()));

		for (OrderMenu orderMenu : orderMenus) {
			s = s + orderMenu.toString();
			if (i < orderMenus.size()-1)
				s = s + "\t";
			i++;
		}

        return s;
    }

	public String getUser() {
		return user.toString();	
	}

	public Date getDeliveryTime() {
		try {
			return Takeaway.dateFormat.parse(deliveryTime);
		} catch(ParseException e) {
			return null;
		}
	}

}
