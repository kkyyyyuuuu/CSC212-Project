package Test;

import java.io.*;

public class CSVLoader {

    public static final String DATA_DIR = "data";

    // غيّرناها بسبب اسم الملف عندك
    public static final String PRODUCTS_PATH  = DATA_DIR + File.separator + "prodcuts.csv";
    public static final String CUSTOMERS_PATH = DATA_DIR + File.separator + "customers.csv";
    public static final String REVIEWS_PATH   = DATA_DIR + File.separator + "reviews.csv";
    public static final String ORDERS_PATH    = DATA_DIR + File.separator + "orders.csv";

    private static <T> void append(List<T> l, T e) {
        if (l == null) return;
        if (l.full()) return;
        if (l.empty()) { l.insert(e); return; }
        l.findFirst();
        while (!l.last()) l.findNext();
        l.insert(e);
    }

    private static boolean isNumeric(String s) {
        if (s == null) return false;
        s = s.trim();
        if (s.length() == 0) return false;
        int i = 0;
        char c0 = s.charAt(0);
        if (c0 == '-' || c0 == '+') i = 1;
        for (; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private static double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }
    }

    // يشيل " " ويصلح "" داخل النص
    private static String unquote(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
        }
        // replace doubled quotes
        String out = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' && i + 1 < s.length() && s.charAt(i + 1) == '"') {
                out += '"';
                i++;
            } else {
                out += c;
            }
        }
        return out;
    }

    private static String quote(String s) {
        if (s == null) s = "";
        String t = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') t += "\"\"";
            else t += c;
        }
        return "\"" + t + "\"";
    }

    // reviews: آخر عمود comment ممكن فيه فواصل، فنقسم أول 4 فواصل فقط
    private static String[] splitReviewLine(String line) {
        String[] parts = new String[5];
        int commas = 0, last = 0;
        for (int i = 0; i < line.length() && commas < 4; i++) {
            if (line.charAt(i) == ',') {
                parts[commas] = line.substring(last, i);
                last = i + 1;
                commas++;
            }
        }
        parts[4] = line.substring(last);
        return parts;
    }

    private static String buildProductIds(List<Product> prods) {
        String s = "";
        if (prods == null || prods.empty()) return s;
        prods.findFirst();
        while (!prods.last()) {
            Product p = prods.retrieve();
            if (p != null) {
                if (s.length() > 0) s += ";";
                s += p.getProductId();
            }
            prods.findNext();
        }
        Product last = prods.retrieve();
        if (last != null) {
            if (s.length() > 0) s += ";";
            s += last.getProductId();
        }
        return s;
    }

    public static void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // ---------------- LOADERS ----------------

    public static void loadProducts() {
        ensureDataDir();
        File f = new File(PRODUCTS_PATH);
        System.out.println("Loading products from: " + f.getAbsolutePath());

        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(f));
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().length() == 0) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;

                String idStr = parts[0].trim();
                if (!isNumeric(idStr)) continue; // header

                int id = parseIntSafe(idStr);
                String name = unquote(parts[1]);
                double price = parseDoubleSafe(parts[2]);
                int stock = parseIntSafe(parts[3]);

                Product p = new Product(id, name, price, stock);
                ProductOperations.addProduct(p); // لو مكرر ID بيتجاهله
            }
            System.out.println("Products loaded.");
        } catch (IOException e) {
            System.out.println("loadProducts error: " + e.getMessage());
        } finally {
            try { if (r != null) r.close(); } catch (IOException ex) {}
        }
    }

    public static void loadCustomers() {
        ensureDataDir();
        File f = new File(CUSTOMERS_PATH);
        System.out.println("Loading customers from: " + f.getAbsolutePath());

        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(f));
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().length() == 0) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;

                String idStr = parts[0].trim();
                if (!isNumeric(idStr)) continue; // header

                int id = parseIntSafe(idStr);
                String name = unquote(parts[1]);
                String email = unquote(parts[2]);

                Customer c = new Customer(id, name, email);
                CustomerOperations.register(c);
            }
            System.out.println("Customers loaded.");
        } catch (IOException e) {
            System.out.println("loadCustomers error: " + e.getMessage());
        } finally {
            try { if (r != null) r.close(); } catch (IOException ex) {}
        }
    }

    public static void loadReviews() {
        ensureDataDir();
        File f = new File(REVIEWS_PATH);
        System.out.println("Loading reviews from: " + f.getAbsolutePath());

        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(f));
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().length() == 0) continue;

                String[] head = line.split(",", 2);
                if (head.length > 0 && !isNumeric(head[0].trim())) continue; // header

                String[] parts = splitReviewLine(line);
                if (parts.length < 5) continue;

                int reviewId   = parseIntSafe(parts[0]);
                int productId  = parseIntSafe(parts[1]);
                int customerId = parseIntSafe(parts[2]);
                int rating     = parseIntSafe(parts[3]);
                String comment = unquote(parts[4]);

                Review rv = new Review(reviewId, productId, customerId, rating, comment);

                Product p = ProductOperations.searchById(productId);
                if (p != null) p.addReview(rv);

                Customer c = CustomerOperations.searchById(customerId);
                if (c != null) c.addReview(rv);
            }
            System.out.println("Reviews loaded.");
        } catch (IOException e) {
            System.out.println("loadReviews error: " + e.getMessage());
        } finally {
            try { if (r != null) r.close(); } catch (IOException ex) {}
        }
    }

    // يدعم: (year,month,day) أو (orderDate=YYYY-MM-DD)
    public static void loadOrders() {
        ensureDataDir();
        File f = new File(ORDERS_PATH);
        System.out.println("Loading orders from: " + f.getAbsolutePath());

        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(f));
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().length() == 0) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;

                String idStr = parts[0].trim();
                if (!isNumeric(idStr)) continue; // header

                int orderId = parseIntSafe(parts[0]);
                int customerId = parseIntSafe(parts[1]);

                // productIds
                String idsStr = unquote(parts[2].trim());
                String[] tokens = (idsStr.length() == 0) ? new String[0] : idsStr.split(";");
                List<Integer> productIds = new LinkedList<Integer>();
                for (int i = 0; i < tokens.length; i++) {
                    String t = tokens[i].trim();
                    if (t.length() == 0) continue;
                    int pid = parseIntSafe(t);
                    append(productIds, Integer.valueOf(pid));
                }

                // date: يا 3 أعمدة أو عمود واحد YYYY-MM-DD
                int year = 0, month = 0, day = 0;

             // الحالة الرسمية: totalPrice ثم orderDate
             if (parts.length >= 5 && parts[4].trim().contains("-")) {
                 String dateStr = parts[4].trim();   // YYYY-MM-DD
                 if (dateStr.length() >= 2 && dateStr.charAt(0) == '"' && dateStr.charAt(dateStr.length()-1) == '"')
                     dateStr = dateStr.substring(1, dateStr.length()-1);

                 String[] d = dateStr.split("-");
                 if (d.length >= 3) {
                     year = parseIntSafe(d[0]);
                     month = parseIntSafe(d[1]);
                     day = parseIntSafe(d[2]);
                 }
             } else {
                 // الحالة القديمة: year,month,day
                 if (parts.length < 6) continue;
                 year  = parseIntSafe(parts[3]);
                 month = parseIntSafe(parts[4]);
                 day   = parseIntSafe(parts[5]);
             }


                // status: آخر عمود لو موجود
                String status = "pending";
                if (parts.length >= 7) status = unquote(parts[parts.length - 1]);

                boolean ok = OrderOperations.createOrder(orderId, customerId, productIds, year, month, day);
                if (ok) OrderOperations.updateOrderStatus(orderId, status);
            }
            System.out.println("Orders loaded.");
        } catch (IOException e) {
            System.out.println("loadOrders error: " + e.getMessage());
        } finally {
            try { if (r != null) r.close(); } catch (IOException ex) {}
        }
    }

    public static void loadAll() {
        loadProducts();
        loadCustomers();
        loadReviews();
        loadOrders();
    }

    // ---------------- SAVERS ----------------
    // (هذي تكتب الملفات من جديد. استخدمها بعد add/update إذا تبي تغييراتك تنحفظ)

    public static void saveProducts() {
        File f = new File(PRODUCTS_PATH);
        System.out.println("saveProducts -> " + f.getAbsolutePath());

        FileWriter w = null;
        try {
            w = new FileWriter(f, false);
            w.write("productId,name,price,stock\n");

            List<Product> L = ProductOperations.allTheProducts;
            if (L != null && !L.empty()) {
                L.findFirst();
                while (!L.last()) {
                    Product p = L.retrieve();
                    if (p != null) {
                        w.write(p.getProductId() + "," + quote(p.getName()) + "," + p.getPrice() + "," + p.getStock() + "\n");
                    }
                    L.findNext();
                }
                Product p = L.retrieve();
                if (p != null) {
                    w.write(p.getProductId() + "," + quote(p.getName()) + "," + p.getPrice() + "," + p.getStock() + "\n");
                }
            }
            w.close();
            System.out.println("saveProducts: done.");
        } catch (IOException e) {
            System.out.println("saveProducts error: " + e.getMessage());
            try { if (w != null) w.close(); } catch (IOException ex) {}
        }
    }

    public static void saveOrders() {
        File f = new File(ORDERS_PATH);
        FileWriter w = null;
        try {
            w = new FileWriter(f, false);
            // نحافظ على صيغة بسيطة ثابتة (لو العمادة عندها صيغة ثانية، لا تستخدم saveOrders)
            w.write("orderId,customerId,productIds,year,month,day,status\n");

            List<Order> L = OrderOperations.allTheOrders;
            if (L != null && !L.empty()) {
                L.findFirst();
                while (!L.last()) {
                    writeOrderLine(w, L.retrieve());
                    L.findNext();
                }
                writeOrderLine(w, L.retrieve());
            }
            w.close();
            System.out.println("saveOrders: done.");
        } catch (IOException e) {
            System.out.println("saveOrders error: " + e.getMessage());
            try { if (w != null) w.close(); } catch (IOException ex) {}
        }
    }

    private static void writeOrderLine(FileWriter w, Order o) throws IOException {
        if (o == null) return;
        String ids = buildProductIds(o.getProducts());
        String line = o.getOrderId() + "," + o.getCustomerId() + "," + ids + "," +
                      o.getYear() + "," + o.getMonth() + "," + o.getDay() + "," + quote(o.getStatus());
        w.write(line + "\n");
    }
}
