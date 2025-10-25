public class Customer {

    private int customerId;
    private String name;
    private String email;
    private CustomDynamicArray<Order> ordersList;
    
    // سمة مضافة لزيادة كفاءة استخلاص المراجعات
    private CustomDynamicArray<Review> reviewsByCustomer; 

    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.ordersList = new CustomDynamicArray<>();
        this.reviewsByCustomer = new CustomDynamicArray<>();
    }

    public void placeNewOrder(Order order) {
        this.ordersList.add(order);
    }
    
    public void addReview(Review review) {
        this.reviewsByCustomer.add(review);
    }

    public CustomDynamicArray<Order> viewOrderHistory() {
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
    
    public CustomDynamicArray<Review> getReviewsByCustomer() {
        return reviewsByCustomer;
    }
    
    public CustomDynamicArray<Order> getOrdersList() {
        return ordersList;
    }
}
