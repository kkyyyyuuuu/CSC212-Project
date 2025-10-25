public class Product {

    private int productId;
    private String name;
    private double price;
    private int stock;
    
    // قائمة المراجعات تستخدم الهيكل المخصص
    private CustomDynamicArray<Review> reviews;

    public Product(int productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.reviews = new CustomDynamicArray<>();
    }

    // --- العمليات المطلوبة ---

    // O: Add/remove/update products
    public void addReview(Review review) {
        if (review != null) {
            this.reviews.add(review);
        }
    }

    // O: Get an average rating for product - O(R_i)
    public double calculateAverageRating() {
        if (reviews.getSize() == 0) {
            return 0.0;
        }

        double totalRating = 0.0;
        
        for (int i = 0; i < reviews.getSize(); i++) {
            totalRating += reviews.get(i).getRatingScore(); 
        }

        return totalRating / reviews.getSize();
    }

    // O: Track out-of-stock products - O(1)
    public boolean isOutOfStock() {
        return this.stock <= 0;
    }
    
    // لتحديث المخزون (Update stock)
    public void updateStock(int quantityChange) {
        this.stock += quantityChange; 
    }
    
    // لتعديل السعر والاسم (جزء من O: Add/remove/update products)
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    // --- Getters ---

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
    
    public CustomDynamicArray<Review> getReviews() {
        return reviews;
    }
}
