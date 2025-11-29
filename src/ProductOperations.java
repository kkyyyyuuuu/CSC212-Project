package Test;

public class ProductOperations {
	public static List<Product> allTheProducts = new LinkedList<Product>();
	public static BST<Product> productsById = new BST<Product>();
	public static BST<Product> productsByPrice = new BST<Product>();

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
    private static int priceKey(double price) {
        // نحول السعر إلى "هللات" كعدد صحيح
        return (int)(price * 100 + 0.5);
    }

    // عشان ما يصير تعارض لو فيه منتجات بنفس السعر
    private static int priceKeyUnique(Product p) {
        return priceKey(p.getPrice()) * 100000 + (p.getProductId() % 100000);
    }

    
    public static boolean addProduct(Product p) {
        if (p == null) return false;

        if (!productsById.insert(p.getProductId(), p)) return false; // duplicate id

        productsByPrice.insert(priceKeyUnique(p), p);
        append(allTheProducts, p);
        return true;
    }


    public static boolean removeById(int id) {
        Product p = productsById.search(id);
        if (p == null) return false;

        productsById.delete(id);
        productsByPrice.delete(priceKeyUnique(p));

        // remove from list (زي كودك)
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
        if (minPrice > maxPrice) return res;

        int minC = priceKey(minPrice);
        int maxC = priceKey(maxPrice);

        // نفس فكرة unique: نغطي كل المنتجات داخل الرينج
        int minKey = minC * 100000;
        int maxKey = maxC * 100000 + 99999;

        // لازم يكون عندك rangeQuery يرجّع List/LinkedList
        List<Product> tmp = productsByPrice.rangeQuery(minKey, maxKey);

        // انسخ النتائج إلى LinkedList
        if (tmp != null && !tmp.empty()) {
            tmp.findFirst();
            while (!tmp.last()) {
                append(res, tmp.retrieve());
                tmp.findNext();
            }
            append(res, tmp.retrieve());
        }
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
        if (p == null) return false;

        // احذف المفتاح القديم من BST السعر
        productsByPrice.delete(priceKeyUnique(p));

        // غيّر السعر
        p.setPrice(newPrice);

        // أعد إدخاله بالمفتاح الجديد
        productsByPrice.insert(priceKeyUnique(p), p);

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
    // Advanced Query #3: Top 3 Highest Rated Products (BST traversal)
    public static LinkedList<Product> top3HighestRatedProducts() {
        LinkedList<Product> res = new LinkedList<Product>();
        if (productsById.empty()) return res;

        LinkedList<Product> all = productsById.inOrderTraversal(); // BST traversal

        Product best1 = null, best2 = null, best3 = null;

        if (!all.empty()) {
            all.findFirst();
            while (true) {
                Product p = all.retrieve();
                if (p != null) {
                    double r = p.calculateAverageRating();

                    double r1 = (best1 == null) ? -1.0 : best1.calculateAverageRating();
                    double r2 = (best2 == null) ? -1.0 : best2.calculateAverageRating();
                    double r3 = (best3 == null) ? -1.0 : best3.calculateAverageRating();

                    if (r > r1) {
                        best3 = best2;
                        best2 = best1;
                        best1 = p;
                    } else if (r > r2) {
                        best3 = best2;
                        best2 = p;
                    } else if (r > r3) {
                        best3 = p;
                    }
                }

                if (all.last()) break;
                all.findNext();
            }
        }

        if (best1 != null) append(res, best1);
        if (best2 != null) append(res, best2);
        if (best3 != null) append(res, best3);

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
 // Query 5-A: sorted by customerId
    public static LinkedList<Customer> customersWhoReviewedProductByCustomerId(int productId) {
        LinkedList<Customer> res = new LinkedList<Customer>();

        Product p = searchById(productId); // productsById.search
        if (p == null) return res;

        List<Review> revs = p.getReviews();
        if (revs == null || revs.empty()) return res;

        BST<Customer> temp = new BST<Customer>(); // key = customerId

        revs.findFirst();
        while (!revs.last()) {
            Review r = revs.retrieve();
            if (r != null) {
                int cid = r.getCustomerId();
                Customer c = CustomerOperations.searchById(cid);
                if (c != null) temp.insert(cid, c);
            }
            revs.findNext();
        }
        Review r = revs.retrieve(); // last
        if (r != null) {
            int cid = r.getCustomerId();
            Customer c = CustomerOperations.searchById(cid);
            if (c != null) temp.insert(cid, c);
        }

        return temp.inOrderTraversal();
    }

    // Query 5-B: sorted by rating (high to low), and if tie then by customerId
    private static int ratingKey(int ratingScore, int customerId) {
        int r = ratingScore;
        if (r < 1) r = 1;
        if (r > 5) r = 5;
        int group = 6 - r; // rating 5 -> 1 (comes first in inorder), rating 1 -> 5
        return group * 100000 + (customerId % 100000);
    }

    public static LinkedList<Customer> customersWhoReviewedProductByRating(int productId) {
        LinkedList<Customer> res = new LinkedList<Customer>();

        Product p = searchById(productId);
        if (p == null) return res;

        List<Review> revs = p.getReviews();
        if (revs == null || revs.empty()) return res;

        BST<Customer> temp = new BST<Customer>(); // key = (rating desc, then customerId)

        revs.findFirst();
        while (!revs.last()) {
            Review rv = revs.retrieve();
            if (rv != null) {
                int cid = rv.getCustomerId();
                Customer c = CustomerOperations.searchById(cid);
                if (c != null) {
                    int k = ratingKey(rv.getRatingScore(), cid);
                    temp.insert(k, c);
                }
            }
            revs.findNext();
        }
        Review rv = revs.retrieve(); // last
        if (rv != null) {
            int cid = rv.getCustomerId();
            Customer c = CustomerOperations.searchById(cid);
            if (c != null) {
                int k = ratingKey(rv.getRatingScore(), cid);
                temp.insert(k, c);
            }
        }

        return temp.inOrderTraversal();
    }


}
