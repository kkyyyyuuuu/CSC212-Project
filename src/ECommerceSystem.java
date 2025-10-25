import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ECommerceSystem {
    
    // هياكل البيانات الرئيسية للتخزين
    private CustomDynamicArray<Product> products;
    private CustomDynamicArray<Customer> customers;
    private CustomDynamicArray<Order> orders;
    
    public ECommerceSystem() {
        this.products = new CustomDynamicArray<>();
        this.customers = new CustomDynamicArray<>();
        this.orders = new CustomDynamicArray<>();
    }

    // --- الدوال المساعدة للبحث (تُستخدم في الربط وحل المتطلبات) ---
    
    // O: Search by ID or name (linear) - O(P)
    public Product searchProductByID(int id) {
        for (int i = 0; i < products.getSize(); i++) {
            Product p = products.get(i);
            if (p.getProductId() == id) {
                return p;
            }
        }
        return null;
    }

    // O: Search order by ID - O(O)
    public Order searchOrderByID(int id) {
        for (int i = 0; i < orders.getSize(); i++) {
            Order o = orders.get(i);
            if (o.getOrderId() == id) {
                return o;
            }
        }
        return null;
    }

    // O: Register new customer / Place a new order
    public Customer searchCustomerByID(int id) {
        for (int i = 0; i < customers.getSize(); i++) {
            Customer c = customers.get(i);
            if (c.getCustomerId() == id) {
                return c;
            }
        }
        return null;
    }
    
    // --- المتطلب 1: قراءة البيانات من ملفات CSV وربطها ---
    
    public void readDataFromCSV() {
        readProducts("data/prodcuts.csv");
        readCustomers("data/customers.csv");
        readReviewsAndLink("data/reviews.csv");
        readOrdersAndLink("data/orders.csv");
    }

    private void readProducts(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); 
                products.add(new Product(
                    Integer.parseInt(values[0]), // productId
                    values[1],                   // name
                    Double.parseDouble(values[2]), // price
                    Integer.parseInt(values[3])  // stock
                ));
            }
        } catch (IOException e) {
            System.err.println("Error reading products: " + e.getMessage());
        }
    }

    private void readCustomers(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                customers.add(new Customer(
                    Integer.parseInt(values[0]), // customerId
                    values[1],                   // name
                    values[2]                    // email
                ));
            }
        } catch (IOException e) {
            System.err.println("Error reading customers: " + e.getMessage());
        }
    }

    private void readReviewsAndLink(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Review newReview = new Review(
                    Integer.parseInt(values[0]), // reviewId
                    Integer.parseInt(values[1]), // productId
                    Integer.parseInt(values[2]), // customerId
                    Integer.parseInt(values[3]), // rating
                    values[4].replace("\"", "")  // comment (إزالة علامات التنصيص)
                );
                
                // ربط المراجعة بالمنتج والعميل (مطلوب لزيادة الكفاءة)
                Product product = searchProductByID(newReview.getProductId());
                Customer customer = searchCustomerByID(newReview.getCustomerId());
                
                if (product != null) {
                    product.addReview(newReview);
                }
                if (customer != null) {
                    customer.addReview(newReview);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading reviews: " + e.getMessage());
        }
    }

    private void readOrdersAndLink(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); 
                
                // تحليل قائمة المنتجات (Product IDs)
                String[] pIdsStr = values[2].replace("\"", "").split(";");
                CustomDynamicArray<Integer> productIds = new CustomDynamicArray<>();
                for (String idStr : pIdsStr) {
                    productIds.add(Integer.parseInt(idStr));
                }

                Order newOrder = new Order(
                    Integer.parseInt(values[0]),  // orderId
                    Integer.parseInt(values[1]),  // customerId
                    productIds,                   // list of product Ids
                    Double.parseDouble(values[3]),// totalPrice
                    values[4],                    // orderDate
                    values[5]                     // status
                );
                
                this.orders.add(newOrder);

                // ربط الطلب بالعميل
                Customer customer = searchCustomerByID(newOrder.getCustomerReference());
                if (customer != null) {
                    customer.placeNewOrder(newOrder);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading orders: " + e.getMessage());
        }
    }
    
    // --- المتطلب 4: استخلاص مراجعات عميل محدد (O(C)) ---
    
    public CustomDynamicArray<Review> getReviewsForCustomer(int customerId) {
        Customer customer = searchCustomerByID(customerId); 
        if (customer != null) {
            // يتم الاسترجاع فوراً بفضل خاصية reviewsByCustomer
            return customer.getReviewsByCustomer(); 
        }
        return new CustomDynamicArray<>(); 
    }

    // --- المتطلب 5: اقتراح "أفضل 3 منتجات" بمتوسط التقييم (O(R + P^2)) ---
    
    // كلاس مساعد مؤقت لربط المنتج بمتوسط تقييمه لأغراض الفرز
    private class ProductRating {
        Product product;
        double averageRating;
        
        public ProductRating(Product p, double avg) {
            this.product = p;
            this.averageRating = avg;
        }
        public Product getProduct() { return product; }
        public double getAverageRating() { return averageRating; }
    }
    
    public CustomDynamicArray<Product> getTop3ProductsByRating() {
        int P = products.getSize();
        if (P == 0) return new CustomDynamicArray<>();
        
        // 1. حساب المتوسطات وتخزينها (O(R))
        CustomDynamicArray<ProductRating> ratings = new CustomDynamicArray<>();
        for (int i = 0; i < P; i++) {
            Product p = products.get(i);
            double avg = p.calculateAverageRating();
            ratings.add(new ProductRating(p, avg));
        }

        // 2. الفرز المخصص (Selection Sort) - O(P^2)
        // يتم فرز الـ ratings ترتيباً تنازلياً
        for (int i = 0; i < P - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < P; j++) {
                if (ratings.get(j).getAverageRating() > ratings.get(maxIdx).getAverageRating()) {
                    maxIdx = j;
                }
            }
            // تبديل (Swap) - يتطلب استخدام set() في CustomDynamicArray
            if (maxIdx != i) {
                ProductRating temp = ratings.get(maxIdx);
                ratings.set(maxIdx, ratings.get(i)); 
                ratings.set(i, temp);
            }
        }

        // 3. استخراج أفضل 3
        CustomDynamicArray<Product> top3 = new CustomDynamicArray<>();
        for (int i = 0; i < 3 && i < P; i++) {
            top3.add(ratings.get(i).getProduct());
        }
        return top3;
    }

    // --- المتطلب 6: جميع الطلبات بين تاريخين (O(O)) ---
    
    public CustomDynamicArray<Order> getOrdersBetweenDates(String startDate, String endDate) {
        CustomDynamicArray<Order> result = new CustomDynamicArray<>();
        
        for (int i = 0; i < orders.getSize(); i++) {
            Order order = orders.get(i);
            String orderDate = order.getOrderDate();
            
            // مقارنة السلاسل النصية للتاريخ (YYYY-MM-DD)
            if (orderDate.compareTo(startDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
                result.add(order);
            }
        }
        return result;
    }
    
    // --- المتطلب 7: المنتجات المشتركة بمتوسط تقييم > 4 لعميلين ---
    
    // دالة مساعدة: للبحث الخطي عن ID في قائمة ID - O(N)
    private boolean containsProductId(CustomDynamicArray<Integer> pIds, int id) {
        for (int i = 0; i < pIds.getSize(); i++) {
            if (pIds.get(i) == id) return true;
        }
        return false;
    }

    // دالة مساعدة: استخلاص معرفات المنتجات الفريدة - O(R_i^2)
    private CustomDynamicArray<Integer> extractUniqueProductIds(CustomDynamicArray<Review> reviews) {
        CustomDynamicArray<Integer> uniqueIds = new CustomDynamicArray<>();
        for (int i = 0; i < reviews.getSize(); i++) {
            int pid = reviews.get(i).getProductId();
            if (!containsProductId(uniqueIds, pid)) { 
                uniqueIds.add(pid);
            }
        }
        return uniqueIds;
    }
    
    // المتطلب الرئيسي - O(C + p1 * p2 + p_common * P)
    public CustomDynamicArray<Product> getCommonHighRatedProducts(int customerId1, int customerId2) {
        CustomDynamicArray<Product> commonProducts = new CustomDynamicArray<>();
        
        Customer c1 = searchCustomerByID(customerId1);
        Customer c2 = searchCustomerByID(customerId2);
        if (c1 == null || c2 == null) return commonProducts;

        // 1. استخلاص مجموعات المنتجات الفريدة
        CustomDynamicArray<Integer> pIds1 = extractUniqueProductIds(c1.getReviewsByCustomer());
        CustomDynamicArray<Integer> pIds2 = extractUniqueProductIds(c2.getReviewsByCustomer());

        // 2. إيجاد التقاطع والتحقق من التقييم
        for (int i = 0; i < pIds1.getSize(); i++) {
            int pid1 = pIds1.get(i);
            
            if (containsProductId(pIds2, pid1)) { 
                Product commonProduct = searchProductByID(pid1);
                
                if (commonProduct != null) {
                    // حساب المتوسط والتحقق: متوسط تقييم > 4
                    if (commonProduct.calculateAverageRating() > 4.0) {
                        commonProducts.add(commonProduct);
                    }
                }
            }
        }
        return commonProducts;
    }
}
