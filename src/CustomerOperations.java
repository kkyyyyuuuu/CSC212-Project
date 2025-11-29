package Test;

public class CustomerOperations {
	public static List<Customer> allTheCustomers = new LinkedList<Customer>();
    public static BST<Customer> customersById = new BST<Customer>();
    public static BST<Customer> customersByName = new BST<Customer>();

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
    private static int nameKey(String name) {
        if (name == null) return 0;
        String s = name.toLowerCase();

        int key = 0;
        int limit = (s.length() < 6) ? s.length() : 6;  // أول 6 أحرف فقط
        for (int i = 0; i < limit; i++) {
            char c = s.charAt(i);
            int v = (c >= 'a' && c <= 'z') ? (c - 'a' + 1) : 0; // غير a-z = 0
            key = key * 27 + v;
        }
        return key;
    }

    // يخلي المفتاح Unique لو تكرر الاسم
    private static int nameKeyUnique(Customer c) {
        return nameKey(c.getName()) * 100000 + (c.getCustomerId() % 100000);
    }



   

    public static void register(Customer c) {
        if (c == null) return;

        if (!customersById.insert(c.getCustomerId(), c)) return;

        customersByName.insert(nameKeyUnique(c), c);  // keyed by name (int)
        append(allTheCustomers, c);
    }


    public static LinkedList<Customer> customersSortedAlphabetically() {
        return customersByName.inOrderTraversal();
    }





 public static Customer searchById(int id) {
        return customersById.search(id);}


    public static boolean placeOrderSpecficCustomer(int customerId, Order order) {
        Customer c = searchById(customerId);
        if (c == null)
            return false;
        c.placeNewOrder(order);
        return true;
    } 
}
