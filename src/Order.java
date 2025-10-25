public class Order {

    private int orderId;
    private int customerReference;

    private ArrayList<Integer> productIds; 
    private double totalPrice;
    private String orderDate;
    private String status;

    public Order(int orderId, int customerReference, ArrayList<Integer> productIds, double totalPrice, String orderDate, String status) {
        this.orderId = orderId;
        this.customerReference = customerReference;
        this.productIds = productIds;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    public void cancelOrder() {
        this.status = "canceled";
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerReference() {
        return customerReference;
    }

    public ArrayList<Integer> getProductIds() {
        return productIds;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }
}
