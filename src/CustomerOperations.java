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
        int taken = 0;

        for (int i = 0; i < s.length() && taken < 3; i++) { // حطيناها ثلاث عشان الاوفر فلو, جربنا 6 ولا نفع
            char c = s.charAt(i);
            if (c < 'a' || c > 'z') continue; // تجاهل المسافات والرموز
            int v = (c - 'a' + 1);
            key = key * 27 + v;
            taken++;
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
    public static List<Order> CustomerOrderHistory(int customerId) {
        Customer c = searchById(customerId);
        if (c == null) return new LinkedList<Order>();
        return c.viewOrderHistory();
    }

}
