public class Order {
    private int orderId;
    private int customerId;
    private List<Product> products;  
    private double totalPrice;
    private int year;
    private int month;    
    private int day;    
    private String status;           

    public Order(int orderId, int customerId, int year, int month, int day) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.status = "pending";
        this.products = new LinkedList<Product>();
        this.totalPrice = 0.0;
    }

    public void addProduct(Product p) {
        if (p == null) return;
        if (products.empty()) { products.insert(p); }
        else {
            products.findFirst();
            while (!products.last())
            { 
            	products.findNext(); 
            	}
            products.insert(p);
        }
        this.totalPrice = this.totalPrice + p.getPrice();
    }
    public int getOrderId() {
    	return orderId;
    	}
    public int getCustomerId() {
    	return customerId; 
    	}
    public int getYear() {
    	return year; 
    	}
    public int getMonth() {
    	return month;
    	}
    public int getDay() { 
    	return day; 
    	}
    public String getStatus() { 
    	return status; 
    	}
    public double getTotalPrice() { 
    	return totalPrice; 
    	}
    public List<Product> getProducts() { 
    	return products; 
    	}

    public void setStatus(String s) {
        if (s == null) 
        	this.status = "pending";
        else 
        	this.status = s;
    }

    public String toString() {
        return "Order{id=" + orderId + ", cust=" + customerId +
               ", date=" + year + "-" + month + "-" + day +
               ", status='" + status + "', total=" + totalPrice + "}";
    }
}
