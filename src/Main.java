public class Main {
    
    private static final int MAX_LIST_SIZE = 1000;

    public static Customer searchCustomerByID(ArrayList<Customer> customers, int id) {
        if (customers.empty()) return null;
        
        customers.findFirst();
        while (true) {
            if (customers.retrieve().getCustomerId() == id) {
                return customers.retrieve();
            }
            if (customers.last()) break;
            customers.findNext();
        }
        return null;
    }

    public static Product searchProductByID(ArrayList<Product> products, int id) {
        if (products.empty()) return null;
        
        products.findFirst();
        while (true) {
            if (products.retrieve().getProductId() == id) {
                return products.retrieve();
            }
            if (products.last()) break;
            products.findNext();
        }
        return null;
    }

    public static void readDataFromCSV(ArrayList<Product> products, ArrayList<Customer> customers, ArrayList<Order> orders) {
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("data/prodcuts.csv"))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); 
                products.insert(new Product(
                    java.lang.Integer.parseInt(values[0]),
                    values[1],
                    java.lang.Double.parseDouble(values[2]),
                    java.lang.Integer.parseInt(values[3])
                ));
            }
        } catch (java.io.IOException e) { java.lang.System.err.println("Error reading products: " + e.getMessage()); }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("data/customers.csv"))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                customers.insert(new Customer(
                    java.lang.Integer.parseInt(values[0]),
                    values[1],
                    values[2]
                ));
            }
        } catch (java.io.IOException e) { java.lang.System.err.println("Error reading customers: " + e.getMessage()); }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("data/reviews.csv"))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Review newReview = new Review(
                    java.lang.Integer.parseInt(values[0]),
                    java.lang.Integer.parseInt(values[1]),
                    java.lang.Integer.parseInt(values[2]),
                    java.lang.Integer.parseInt(values[3]),
                    values[4].replace("\"", "")
                );
                
                Product product = searchProductByID(products, newReview.getProductId());
                Customer customer = searchCustomerByID(customers, newReview.getCustomerId());
                
                if (product != null) { product.addReview(newReview); }
                if (customer != null) { customer.addReview(newReview); }
            }
        } catch (java.io.IOException e) { java.lang.System.err.println("Error reading reviews: " + e.getMessage()); }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("data/orders.csv"))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); 
                
                String[] pIdsStr = values[2].replace("\"", "").split(";");
                ArrayList<Integer> productIds = new ArrayList<>(pIdsStr.length);
                for (String idStr : pIdsStr) {
                    productIds.insert(java.lang.Integer.parseInt(idStr));
                }

                Order newOrder = new Order(
                    java.lang.Integer.parseInt(values[0]),
                    java.lang.Integer.parseInt(values[1]),
                    productIds,
                    java.lang.Double.parseDouble(values[3]),
                    values[4],
                    values[5]
                );
                
                orders.insert(newOrder);

                Customer customer = searchCustomerByID(customers, newOrder.getCustomerReference());
                if (customer != null) { customer.placeNewOrder(newOrder); }
            }
        } catch (java.io.IOException e) { java.lang.System.err.println("Error reading orders: " + e.getMessage()); }
    }
    
    private static class ProductRating {
        Product product;
        double averageRating;
        
        public ProductRating(Product p, double avg) {
            this.product = p;
            this.averageRating = avg;
        }
        public Product getProduct() { return product; }
        public double getAverageRating() { return averageRating; }
    }
    
    public static ArrayList<Product> getTop3ProductsByRating(ArrayList<Product> products) {
        if (products.empty()) return new ArrayList<>(3);
        
        ArrayList<ProductRating> ratings = new ArrayList<>(products.size);
        
        products.findFirst();
        while (true) {
            Product p = products.retrieve();
            double avg = p.calculateAverageRating();
            ratings.insert(new ProductRating(p, avg));
            if (products.last()) break;
            products.findNext();
        }
        
        ArrayList<Product> top3 = new ArrayList<>(3);
        
        ratings.findFirst();
        for (int i = 0; i < 3 && i < ratings.size; i++) {
            top3.insert(ratings.retrieve().getProduct());
            if (ratings.last()) break;
            ratings.findNext();
        }
        
        return top3;
    }
    
    public static ArrayList<Order> getOrdersBetweenDates(ArrayList<Order> orders, String startDate, String endDate) {
        ArrayList<Order> result = new ArrayList<>(orders.size);
        
        if (orders.empty()) return result;
        
        orders.findFirst();
        while (true) {
            Order order = orders.retrieve();
            String orderDate = order.getOrderDate();
            
            if (orderDate.compareTo(startDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
                result.insert(order);
            }
            if (orders.last()) break;
            orders.findNext();
        }
        return result;
    }
    
    private static boolean containsProductId(ArrayList<Integer> pIds, int id) {
        if (pIds.empty()) return false;
        pIds.findFirst();
        while (true) {
            if (pIds.retrieve() == id) return true;
            if (pIds.last()) break;
            pIds.findNext();
        }
        return false;
    }

    private static ArrayList<Integer> extractUniqueProductIds(DoubleLinkedList<Review> reviews) {
        ArrayList<Integer> uniqueIds = new ArrayList<>(reviews.getSize());
        
        if (reviews.empty()) return uniqueIds;
        
        reviews.findFirst();
        while (true) {
            int pid = reviews.retrieve().getProductId();
            
            boolean found = false;
            if (!uniqueIds.empty()) {
                uniqueIds.findFirst();
                while (true) {
                    if (uniqueIds.retrieve() == pid) {
                        found = true;
                        break;
                    }
                    if (uniqueIds.last()) break;
                    uniqueIds.findNext();
                }
            }
            
            if (!found) {
                uniqueIds.insert(pid);
            }

            if (reviews.last()) break;
            reviews.findNext();
        }
        return uniqueIds;
    }
    
    public static ArrayList<Product> getCommonHighRatedProducts(ArrayList<Product> products, ArrayList<Customer> customers, int customerId1, int customerId2) {
        ArrayList<Product> commonProducts = new ArrayList<>(MAX_LIST_SIZE);
        
        Customer c1 = searchCustomerByID(customers, customerId1);
        Customer c2 = searchCustomerByID(customers, customerId2);
        if (c1 == null || c2 == null) return commonProducts;

        ArrayList<Integer> pIds1 = extractUniqueProductIds(c1.getReviewsByCustomer());
        ArrayList<Integer> pIds2 = extractUniqueProductIds(c2.getReviewsByCustomer());

        if (pIds1.empty()) return commonProducts;
        
        pIds1.findFirst();
        while (true) {
            int pid1 = pIds1.retrieve();
            
            boolean isCommon = false;
            if (!pIds2.empty()) {
                pIds2.findFirst();
                while(true) {
                    if(pIds2.retrieve() == pid1) {
                        isCommon = true;
                        break;
                    }
                    if (pIds2.last()) break;
                    pIds2.findNext();
                }
            }
            
            if (isCommon) { 
                Product commonProduct = searchProductByID(products, pid1);
                
                if (commonProduct != null) {
                    if (commonProduct.calculateAverageRating() > 4.0) {
                        commonProducts.insert(commonProduct);
                    }
                }
            }
            
            if (pIds1.last()) break;
            pIds1.findNext();
        }
