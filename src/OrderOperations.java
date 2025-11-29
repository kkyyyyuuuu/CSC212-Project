package Test;

public class OrderOperations {

	public static List<Order> allTheOrders = new LinkedList<Order>();
	public static BST<Order> ordersById = new BST<Order>();
	public static BST<Order> ordersByDate = new BST<Order>();

	private static <T> void append(List<T> l, T e) {
		if (l.full())
			return;
		if (l.empty()) {
			l.insert(e);
			return;
		}
		l.findFirst();
		while (!l.last())
			l.findNext();
		l.insert(e);
	}

	private static int dateKey(int y, int m, int d) {
		return y * 10000 + m * 100 + d;
	}

	// Unique even if same date (مثل nameKeyUnique عندكم)
	private static int dateKeyUnique(Order o) {
		return dateKey(o.getYear(), o.getMonth(), o.getDay()) * 100000 + (o.getOrderId() % 100000);
	}

	public static Order searchById(int orderId) {
	    return ordersById.search(orderId);
	}


	public static boolean createOrder(int orderId, int customerId, List<Integer> productIds, int year, int month,
			int d11y) {

		Customer c = CustomerOperations.searchById(customerId);
		if (c == null)
			return false;

		Order o = new Order(orderId, customerId, year, month, d11y);

// keyed by orderId (BST)
		if (!ordersById.insert(orderId, o))
			return false;

		if (productIds != null && !productIds.empty()) {
			productIds.findFirst();
			while (!productIds.last()) {
				Integer pid = productIds.retrieve();
				if (pid != null) {
					Product p = ProductOperations.searchById(pid.intValue());
					if (p != null) {
						o.addProduct(p);
						p.updateStock(-1);
					}
				}
				productIds.findNext();
			}
			Integer pid = productIds.retrieve();
			if (pid != null) {
				Product p = ProductOperations.searchById(pid.intValue());
				if (p != null) {
					o.addProduct(p);
					p.updateStock(-1);
				}
			}
		}

		ordersByDate.insert(dateKeyUnique(o), o);

		append(allTheOrders, o);
		c.placeNewOrder(o);
		return true;
	}

	public static boolean cancelOrder(int orderId) {
		Order o = searchById(orderId);
		if (o == null)
			return false;
		o.setStatus("cancelled");
		return true;
	}

	public static boolean updateOrderStatus(int orderId, String newStatus) {
		Order o = searchById(orderId);
		if (o == null)
			return false;
		o.setStatus(newStatus);
		return true;
	}
    // between dates using BST rangeQuery (in-order traversal)
	public static LinkedList<Order> ordersBetween(int y1, int m1, int d1, int y2, int m2, int d2) {
        int low = dateKey(y1, m1, d1) * 100000;
        int high = dateKey(y2, m2, d2) * 100000 + 99999;
        return ordersByDate.rangeQuery(low, high);
    }
}
