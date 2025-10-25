public class ProductOperations {
    private static <T> void append(List<T> l, T e) {
        if (l.full())
        	return;
        if (l.empty())
        { 
        	l.insert(e); 
        	return; }
        l.findFirst();
        while (!l.last()) l.findNext();
        l.insert(e);
    }  
    public static void addProduct(List<Product> products, Product p) {
        if (p == null) 
        	return;
        append(products, p);
    }
    public static boolean removeById(List<Product> products, int id) {
        if (products.empty()) 
        	return false;
        products.findFirst();
        Product cur;
        while (!products.last()) {
            cur = products.retrieve();
            if (cur != null) {       
            	if(cur.getProductId() == id) {
            		products.remove(); 
            		return true;
            	}
            }
            products.findNext();
        }
        cur = products.retrieve();
        if (cur != null) {              
        	if(cur.getProductId() == id)
        		products.remove(); 
        		return true;
        	}
        return false;
    }
    
    public static boolean updateName(List<Product> products, int id, String newName) {
        Product p = searchById(products, id);
        if (p == null)
        	return false;
        p.setName(newName);
        products.update(p);
        return true;
    }
    public static boolean updatePrice(List<Product> products, int id, double newPrice) {
        Product p = searchById(products, id);
        if (p == null) 
        	return false;
        p.setPrice(newPrice);
        products.update(p);
        return true;
    }
    public static boolean updateStock(List<Product> products, int id, int newStock) {
        Product p = searchById(products, id);
        if (p == null) 
        	return false;
        int delta = newStock - p.getStock();
        p.updateStock(delta);
        products.update(p);
        return true;
    }   
    public static Product searchById(List<Product> products, int id) {
        if (products.empty())
        	return null;
        Product cur;
        products.findFirst();
        while (!products.last()) {
             cur = products.retrieve();
            if (cur != null) {
            	if(cur.getProductId() == id) {
            		return cur;
            	}
            }
            products.findNext();
        }
        cur = products.retrieve();
        if (cur != null) {
            if (cur.getProductId() == id) {
                return cur;
            }
        }
        return null;
    }
    public static LinkedList<Product> searchByName(List<Product> products, String name) {
        LinkedList<Product> res = new LinkedList<Product>();
        if (products.empty()) 
        	return res;
        String q;
        if (name == null) 
        	q = "";
        else 
        	q = name;
        Product cur;
        products.findFirst();     
        while (!products.last()) {
            cur = products.retrieve();
            if (cur != null) {
            	if(namesEqual(cur.getName(), q)) {           
                append(res, cur);
            }
            }
            products.findNext();
        }       
        cur = products.retrieve();
        if (cur != null && namesEqual(cur.getName(), q)) {
            append(res, cur);
        }
        return res;
    }

    private static boolean namesEqual(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
    public static LinkedList<Product> outOfStock(List<Product> products) {
        LinkedList<Product> res = new LinkedList<Product>();
        if (products.empty()) 
        	return res;
        Product cur;
        products.findFirst();
        while (!products.last()) {
             cur = products.retrieve();
            if (cur != null) {
            	if(cur.isOutOfStock()) 
            		append(res, cur);
            }
            products.findNext();
        }
         cur = products.retrieve();
         if (cur != null) {
         	if(cur.isOutOfStock()) 
         		append(res, cur);
         }
        return res;
    }
}
