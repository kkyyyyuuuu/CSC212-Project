package a;

public class OrderOperations {

    public static List<Order> allTheOrders = new LinkedList<Order>();

    // Helper لإضافة في آخر أي قائمة ADT
    private static <T> void append(List<T> l, T e) {
        if (l.full()) return;
        if (l.empty()) { l.insert(e); return; }
        l.findFirst();
        while (!l.last()) l.findNext();
        l.insert(e);
    }

    // بحث خطي بالـID (نمط المنهج)
    public static Order searchById(int orderId) {
        if (allTheOrders.empty()) return null;

        allTheOrders.findFirst();
        Order cur;
        while (!allTheOrders.last()) {
            cur = allTheOrders.retrieve();
            if (cur != null) {
                if (cur.getOrderId() == orderId) return cur;
            }
            allTheOrders.findNext();
        }
        cur = allTheOrders.retrieve(); // الأخير
        if (cur != null) {
            if (cur.getOrderId() == orderId) return cur;
        }
        return null;
    }

    // إنشاء طلب جديد لعميل محدد (بـ IDs للمنتجات)
    // productIds = قائمة ADT من Integer (مو Collections)
    // التعقيد: O(#products في الطلب + البحث عن كل منتج في كتالوج المنتجات)
    public static boolean createOrder(int orderId,
                                      int customerId,
                                      List<Integer> productIds,
                                      int year, int month, int day) {
        // نلقى العميل
        Customer c = CustomerOperations.searchById(customerId);
        if (c == null) return false;

        // نبني الطلب
        Order o = new Order(orderId, customerId, year, month, day);

        // نضيف المنتجات حسب IDs ثم ننقص المخزون 1 (بسيطة)
        if (productIds != null && !productIds.empty()) {
            productIds.findFirst();
            while (!productIds.last()) {
                Integer pid = productIds.retrieve();
                if (pid != null) {
                    Product p = ProductOperations.searchById(pid.intValue());
                    if (p != null) {
                        o.addProduct(p);
                        p.updateStock(-1); // طلب = قطعة واحدة من كل PID
                    }
                }
                productIds.findNext();
            }
            Integer pid = productIds.retrieve(); // الأخير
            if (pid != null) {
                Product p = ProductOperations.searchById(pid.intValue());
                if (p != null) {
                    o.addProduct(p);
                    p.updateStock(-1);
                }
            }
        }

        // نضيف الطلب لقائمة النظام
        append(allTheOrders, o);

        // ونضيفه لتاريخ العميل
        c.placeNewOrder(o);

        return true;
    }

    // إلغاء طلب
    public static boolean cancelOrder(int orderId) {
        Order o = searchById(orderId);
        if (o == null) return false;
        o.setStatus("canceled");
        return true;
    }

    // تحديث حالة الطلب
    public static boolean updateOrderStatus(int orderId, String newStatus) {
        Order o = searchById(orderId);
        if (o == null) return false;
        o.setStatus(newStatus); // بسيط وواضح (بدون تحقق إضافي)
        return true;
    }

    // كل الطلبات بين تاريخين (شاملين)
    // التعقيد: O(N) حيث N عدد الطلبات
    public static LinkedList<Order> ordersBetween(int y1, int m1, int d1,int y2, int m2, int d2) {
        LinkedList<Order> res = new LinkedList<Order>();
        if (allTheOrders.empty()) return res;

        allTheOrders.findFirst();
        Order cur;
        while (!allTheOrders.last()) {
            cur = allTheOrders.retrieve();
            if (cur != null) {
                if (isInRange(cur.getYear(), cur.getMonth(), cur.getDay(),
                              y1, m1, d1, y2, m2, d2)) {
                    append(res, cur);
                }
            }
            allTheOrders.findNext();
        }
        cur = allTheOrders.retrieve(); // الأخير
        if (cur != null) {
            if (isInRange(cur.getYear(), cur.getMonth(), cur.getDay(),
                          y1, m1, d1, y2, m2, d2)) {
                append(res, cur);
            }
        }

        return res;
    }

    // ===== Helpers للمقارنة بين التواريخ (يدوي وبسيط) =====
    private static boolean isBeforeOrEqual(int ya, int ma, int da,
                                           int yb, int mb, int db) {
        if (ya < yb) return true;
        if (ya > yb) return false;
        // نفس السنة
        if (ma < mb) return true;
        if (ma > mb) return false;
        // نفس الشهر
        if (da <= db) return true;
        return false;
    }

    private static boolean isAfterOrEqual(int ya, int ma, int da,
                                          int yb, int mb, int db) {
        if (ya > yb) return true;
        if (ya < yb) return false;
        // نفس السنة
        if (ma > mb) return true;
        if (ma < mb) return false;
        // نفس الشهر
        if (da >= db) return true;
        return false;
    }

    private static boolean isInRange(int y, int m, int d,
                                     int y1, int m1, int d1,
                                     int y2, int m2, int d2) {
        // y1,m1,d1 <= (y,m,d) <= y2,m2,d2
        if (!isAfterOrEqual(y, m, d, y1, m1, d1)) return false;
        if (!isBeforeOrEqual(y, m, d, y2, m2, d2)) return false;
        return true;
    }}
