package Test;

public class CustomerOperations {

    public static List<Customer> allTheCustomers = new LinkedList<Customer>();
    private static <T> void append(List<T> l, T e) {  
        if (l.full())
        	return;
        if (l.empty())
        { 
        	l.insert(e); 
        	return; }
        l.findFirst();
        while (!l.last())
        	l.findNext();
        l.insert(e);
    }
    public static void register(Customer c) {
        if (c == null) return;
        append(allTheCustomers, c);
    }
    public static Customer searchById(int id) {
        if (allTheCustomers.empty()) return null;

        allTheCustomers.findFirst();
        Customer cur;
        while (!allTheCustomers.last()) {
            cur = allTheCustomers.retrieve();
            if (cur != null) {
                if (cur.getCustomerId() == id) { return cur; }
            }
            allTheCustomers.findNext();
        }
        cur = allTheCustomers.retrieve();
        if (cur != null) {
            if (cur.getCustomerId() == id) { return cur; }
        }
        return null;
    }

    public static boolean placeOrderSpecficCustomer(int customerId, Order order) {
        Customer c = searchById(customerId);
        if (c == null)
        	return false;
        c.placeNewOrder(order);
        return true;
    }
}
