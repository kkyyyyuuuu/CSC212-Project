public class ProductOperations {
	public static List<Product> allTheProducts = new LinkedList<Product>();
	public static BST<Product> productsById = new BST<Product>();
	
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
    
    public static void addProduct(Product p) {
        if (p == null) 
        	return;
     // أول شي: BST by id (لو موجود نفس id لا تكرر)
        if (!productsById.insert(p.getProductId(), p)) return;

        
        
        append(allTheProducts, p);
    }
    public static boolean removeById(int id) {
        Product p = productsById.search(id);
        if (p == null) return false;

        productsById.delete(id);

        // remove from list (نفس كودك القديم)
        if (allTheProducts.empty()) return false;
        allTheProducts.findFirst();
        while (!allTheProducts.last()) {
            Product cur = allTheProducts.retrieve();
            if (cur != null && cur.getProductId() == id) {
                allTheProducts.remove();
                return true;
            }
            allTheProducts.findNext();
        }
        Product cur = allTheProducts.retrieve();
        if (cur != null && cur.getProductId() == id) {
            allTheProducts.remove();
            return true;
        }
        return false;
    }
    public static LinkedList<Product> productsInPriceRange(double minPrice, double maxPrice) {
        LinkedList<Product> res = new LinkedList<Product>();
        if (allTheProducts.empty()) return res;

        if (minPrice > maxPrice) {
            double t = minPrice; minPrice = maxPrice; maxPrice = t;
        }

        allTheProducts.findFirst();
        while (!allTheProducts.last()) {
            Product p = allTheProducts.retrieve();
            if (p != null && p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                append(res, p);
            allTheProducts.findNext();
        }
        Product p = allTheProducts.retrieve();
        if (p != null && p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            append(res, p);

        return res;
    }


    
    public boolean updateName(int id, String newName) {
        Product p = searchById(id);
        if (p == null) 
        	return false;
        p.setName(newName);
        return true;
    }
    public boolean updatePrice(int id, double newPrice) {
        Product p = searchById(id);
        if (p == null)
        	return false;
        

        p.setPrice(newPrice);

        
        return true;
    }
    public boolean updateStock(int id, int newStock) {
        Product p = searchById(id);
        if (p == null) 
        	return false;
        int tmp = newStock - p.getStock();
        p.updateStock(tmp);
        return true;
    }   
    public static Product searchById(int id) {
        return productsById.search(id);
    }
    public static LinkedList<Product> searchByName(String name) {
        LinkedList<Product> res = new LinkedList<Product>();
        if (allTheProducts.empty() || name == null || name.length() == 0)
        	return res;
        String nNInLowerCase = name.toLowerCase();
        Product cur;
        allTheProducts.findFirst();     
        while (!allTheProducts.last()) {
            cur = allTheProducts.retrieve();
            if (cur != null) {
            	String namegetName = cur.getName();
            	if(namegetName != null) {
            		String namegetNameInLowerCase= namegetName.toLowerCase();
            		if(namegetNameInLowerCase.contains(nNInLowerCase)) {
            			append(res, cur);
            		}            	                        	
            	}
            }
            allTheProducts.findNext();
        }       
        cur = allTheProducts.retrieve();
        if (cur != null) {
        	String namegetName = cur.getName();
        	if(namegetName != null) {
        		String namegetNameInLowerCase= namegetName.toLowerCase();
        		if(namegetNameInLowerCase.contains(nNInLowerCase)) {
        			append(res, cur);
        		}
        	}
        }
        return res;
    }
    public static LinkedList<Product> outOfStock() {
        LinkedList<Product> res = new LinkedList<Product>();
        if (allTheProducts.empty()) 
        	return res;
        Product cur;
        allTheProducts.findFirst();
        while (!allTheProducts.last()) {
             cur = allTheProducts.retrieve();
            if (cur != null) {
            	if(cur.isOutOfStock()) 
            		append(res, cur);
            }
            allTheProducts.findNext();
        }
         cur = allTheProducts.retrieve();
         if (cur != null) {
         	if(cur.isOutOfStock()) 
         		append(res, cur);
         }
        return res;
    }
    
    
    public static LinkedList<Product> top3ByAverage() {
        LinkedList<Product> res = new LinkedList<Product>();
        if (allTheProducts.empty()) {
            return res;
        }   
        int size = 0;
        allTheProducts.findFirst();     
        while (!allTheProducts.last()) { 
            if (allTheProducts.retrieve() != null) {
                size++;
            }
            allTheProducts.findNext(); 
        }
        if (allTheProducts.retrieve() != null) { 
            size++;
        }     
        if (size == 0) return res;
        Product[] productArray = new Product[size];
        int i_copy = 0; 
        allTheProducts.findFirst();        
        while (!allTheProducts.last()) { 
            Product p = allTheProducts.retrieve(); 
            if (p != null) {
                productArray[i_copy] = p;
                i_copy++;
            }
            allTheProducts.findNext();
        }
        Product p_last = allTheProducts.retrieve();
        if (p_last != null) {
            productArray[i_copy] = p_last;
        }       
        int n = productArray.length;       
        for (int i= 0; i<n-1; i++) {                      
            int maxIndex = i;
            for (int j= i+1; j<n; j++) {
            	double ratingJ = -1.0;
            	if (productArray[j] != null) {
            	    ratingJ = productArray[j].calculateAverageRating();
            	}

            	double ratingMax = -1.0;
            	if (productArray[maxIndex] != null) {
            	    ratingMax = productArray[maxIndex].calculateAverageRating();
            	}
                if (ratingJ > ratingMax) {
                    maxIndex = j;
                }
            }  
            Product temp = productArray[i];
            productArray[i] = productArray[maxIndex];
            productArray[maxIndex] = temp;
        }             
        if (productArray[0] != null) {
            append(res, productArray[0]);
        }
        if (productArray.length >= 2 && productArray[1] != null) {
            append(res, productArray[1]);
        }
        if (productArray.length >= 3 && productArray[2] != null) {
            append(res, productArray[2]);
        }
        return res;
    }
    public LinkedList<Product> commonReviewedAbove4(int c1, int c2) {
        LinkedList<Product> res = new LinkedList<Product>();
        if (allTheProducts.empty()) return res;
        allTheProducts.findFirst();
        Product p;
        while (!allTheProducts.last()) {
            p = allTheProducts.retrieve();
            if (p != null) {
                if (p.hasReviewFrom(c1)) {
                    if (p.hasReviewFrom(c2)) {
                        if (p.calculateAverageRating() > 4.0) append(res, p);
                    }
                }
            }
            allTheProducts.findNext();
        }
        p = allTheProducts.retrieve();
        if (p != null) {
            if (p.hasReviewFrom(c1) && p.hasReviewFrom(c2) && p.calculateAverageRating() > 4.0) {
                append(res, p);
            }
        }
        return res;
    }
    public LinkedList<Review> collectReviewsByCustomer(int customerId) {
        LinkedList<Review> out = new LinkedList<Review>();
        if (allTheProducts.empty()) return out;

        allTheProducts.findFirst();
        Product p;
        while (!allTheProducts.last()) {
            p = allTheProducts.retrieve();
            if (p != null) {
                List<Review> revs = p.getReviews();
                if (!revs.empty()) {
                    revs.findFirst();
                    Review rv;
                    while (!revs.last()) {
                        rv = revs.retrieve();
                        if (rv != null) {
                            if (rv.getCustomerId() == customerId) append(out, rv);
                        }
                        revs.findNext();
                    }
                    rv = revs.retrieve();
                    if (rv != null) {
                        if (rv.getCustomerId() == customerId) append(out, rv);
                    }
                }
            }
            allTheProducts.findNext();
        }
        p = allTheProducts.retrieve();
        if (p != null) {
            List<Review> revs = p.getReviews();
            if (!revs.empty()) {
                revs.findFirst();
                Review rv;
                while (!revs.last()) {
                    rv = revs.retrieve();
                    if (rv != null) {
                        if (rv.getCustomerId() == customerId) append(out, rv);
                    }
                    revs.findNext();
                }
                rv = revs.retrieve();
                if (rv != null) {
                    if (rv.getCustomerId() == customerId) append(out, rv);
                }
            }
        }
        return out;
    }

}
