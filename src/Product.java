public class Product {
	private int productId;
    private String name;
    private double price;
    private int stock;
    
    private List<Review> reviews;

    public Product(int productId, String name, double price, int stock) {
        this.productId = productId;
 
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.reviews = new LinkedList<Review>();
    }

    public void addReview(Review review) {
        if(review == null) 
        	return;
        if(reviews.empty()) {
            reviews.insert(review);
        } 
        else {
            reviews.findFirst();
            while (!reviews.last()) {
                reviews.findNext();
            }
            reviews.insert(review); 
        }
    }
    

    public double calculateAverageRating() {
        if (reviews.empty()) 
        	return 0.0;
        double total = 0.0;
        int count = 0;
        reviews.findFirst();
        while (!reviews.last()) {
            Review r = reviews.retrieve();
            if (r!=null) {
                total +=r.getRatingScore();
                count++;
            }
            reviews.findNext();
        }
            Review r = reviews.retrieve(); 
        if (r != null) {
            total +=r.getRatingScore();
            count++;
        }
        if(count==0)
        	return 0.0;
        else 
        	return total/count;      
    }



    public boolean isOutOfStock() {
        return stock <= 0;      
    }
    
    public void updateStock(int quantityChange) {
        stock += quantityChange; 
        if (stock <0) {
        	stock=0;
        }
    }
    
    public boolean hasReviewFrom(int customerId) {
        if (reviews.empty()) 
        	return false;
        Review cur;
        reviews.findFirst();
        while (!reviews.last()) {
            cur = reviews.retrieve();
            if (cur != null) {
                if (cur.getCustomerId() == customerId) {
                    return true;
                }
            }
            reviews.findNext();
        }
        cur = reviews.retrieve(); 
        if (cur != null) {
            if (cur.getCustomerId() == customerId) {
                return true;
            }
        }
        return false;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setName(String name) {
        if(name == null) {
        	this.name ="";
        }
        else {
        	this.name = name;
        }
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
    
    public List<Review> getReviews() { 
    	return reviews; 
    }
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", price=" + price + ", stock=" + stock
				+ ", reviews=" + reviews + "]";
	}
     }
