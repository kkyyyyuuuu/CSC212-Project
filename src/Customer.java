public class Customer {

    private int customerId;
    private String name;
    private String email;

    private List<Review> reviewsByCustomer;
    private List<Order>  ordersList;
    
    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.ordersList        = new LinkedList<Order>();
        this.reviewsByCustomer = new LinkedList<Review>();
    }
    public void placeNewOrder(Order order) {
    	if (order == null) 
    		return;
        if (ordersList.empty()) {
        		ordersList.insert(order); 
        		return; 
        }
        ordersList.findFirst();
        while (!ordersList.last()) { 
        	ordersList.findNext(); 
        	}
        ordersList.insert(order);
    }
    public void addReview(Review review) {
        if(review==null) return; 
        if(reviewsByCustomer.empty()) {
        	reviewsByCustomer.insert(review);
        	return; 
        }
        reviewsByCustomer.findFirst();
        while(!reviewsByCustomer.last()) {
        	reviewsByCustomer.findNext();
        }
        reviewsByCustomer.insert(review);	
        }
    public List<Order> viewOrderHistory() {
        return ordersList;
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
    
    public List<Review> getReviewsByCustomer() {
        return reviewsByCustomer;
    }
    
    public List<Order> getOrdersList() {
        return ordersList;
    }
	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", name=" + name + ", email=" + email + ", reviewsByCustomer="
				+ reviewsByCustomer + ", ordersList=" + ordersList + "]";
	}
}
