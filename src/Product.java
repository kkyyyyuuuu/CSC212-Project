public class Product {

    private int productId;
    private String name;
    private double price;
    private int stock;
    

    private DoubleLinkedList<Review> reviews;

    public Product(int productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.reviews = new DoubleLinkedList<>();
    }

    public void addReview(Review review) {
        if (review != null) {
            this.reviews.insert(review);
        }
    }

    public double calculateAverageRating() {
        if (reviews.empty()) {
            return 0.0;
        }

        double totalRating = 0.0;
        int count = 0;
        
        reviews.findFirst();
        while (reviews.current != null) { 
            totalRating += reviews.retrieve().getRatingScore();
            count++;
            if (reviews.last()) break; 
            reviews.findNext();
        }

        return totalRating / count;
    }

    public boolean isOutOfStock() {
        return this.stock <= 0;
    }
    
    public void updateStock(int quantityChange) {
        this.stock += quantityChange; 
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setName(String name) {
        this.name = name;
    }

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
    
    public DoubleLinkedList<Review> getReviews() {
        return reviews;
    }
}
