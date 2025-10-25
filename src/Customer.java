public class Customer {

    private int customerId;
    private String name;
    private String email;

    private DoubleLinkedList<Order> ordersList; 

    private DoubleLinkedList<Review> reviewsByCustomer; 

    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.ordersList = new DoubleLinkedList<>();
        this.reviewsByCustomer = new DoubleLinkedList<>();
    }

    public void placeNewOrder(Order order) {
        this.ordersList.insert(order);
    }
    
    public void addReview(Review review) {
        this.reviewsByCustomer.insert(review);
    }

    public DoubleLinkedList<Order> viewOrderHistory() {
        return this.ordersList;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    
    public DoubleLinkedList<Review> getReviewsByCustomer() {
        return reviewsByCustomer;
    }
    
    public DoubleLinkedList<Order> getOrdersList() {
        return ordersList;
    }
}
