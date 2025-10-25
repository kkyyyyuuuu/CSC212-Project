import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        
        ECommerceSystem system = new ECommerceSystem();
        System.out.println("--- 1. System Initialized ---");

        System.out.println("Reading data from CSV files...");
        system.readDataFromCSV();
        System.out.println("Data loaded successfully!");
        System.out.println("\n----------------------------------------");


        // --- اختبار المتطلبات الوظيفية ---

        // متطلب: اقتراح "أفضل 3 منتجات" بمتوسط التقييم (O(R + P^2))
        System.out.println("--- 2. Top 3 Products by Average Rating ---");
        CustomDynamicArray<Product> topProducts = system.getTop3ProductsByRating();
        if (topProducts.getSize() > 0) {
            for (int i = 0; i < topProducts.getSize(); i++) {
                Product p = topProducts.get(i);
                System.out.printf("%d. %s (ID: %d) - Avg Rating: %.2f\n", 
                                  (i + 1), p.getName(), p.getProductId(), p.calculateAverageRating());
            }
        } else {
            System.out.println("No products found.");
        }
        System.out.println("Complexity Analysis: O(R + P^2) due to custom Selection Sort.");
        System.out.println("\n----------------------------------------");


        // متطلب: جميع الطلبات بين تاريخين (O(O))
        String startDate = "2025-02-15";
        String endDate = "2025-02-28";
        System.out.println("--- 3. Orders Between Dates: " + startDate + " and " + endDate + " ---");
        CustomDynamicArray<Order> filteredOrders = system.getOrdersBetweenDates(startDate, endDate);
        System.out.println("Found " + filteredOrders.getSize() + " orders in this range.");
        
        for (int i = 0; i < 3 && i < filteredOrders.getSize(); i++) {
            Order o = filteredOrders.get(i);
            System.out.println("Order ID: " + o.getOrderId() + ", Customer ID: " + o.getCustomerReference() + ", Date: " + o.getOrderDate());
        }
        System.out.println("Complexity Analysis: O(O) due to linear scan of all orders.");
        System.out.println("\n----------------------------------------");


        // متطلب: استخلاص مراجعات عميل محدد (O(C))
        int testCustomerId = 201; 
        System.out.println("--- 4. Reviews for Customer ID: " + testCustomerId + " ---");
        CustomDynamicArray<Review> customerReviews = system.getReviewsForCustomer(testCustomerId);
        System.out.println("Customer " + testCustomerId + " wrote " + customerReviews.getSize() + " reviews.");
        
        for (int i = 0; i < 2 && i < customerReviews.getSize(); i++) {
            Review r = customerReviews.get(i);
            System.out.println("  - Review ID: " + r.getReviewId() + ", Product ID: " + r.getProductId() + ", Rating: " + r.getRatingScore());
        }
        System.out.println("Complexity Analysis: O(C) because reviews are pre-linked to the Customer object.");
        System.out.println("\n----------------------------------------");


        // متطلب: المنتجات المشتركة بمتوسط تقييم > 4 لعميلين (O(C + p
