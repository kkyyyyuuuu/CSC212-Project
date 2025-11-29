package Test;

import java.util.Scanner;

public class Main {

    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {

        // 1) Load data (CSV)
        CSVLoader.loadAll();

        while (true) {
            printMenu();
            int choice = readInt("Choose: ");

            if (choice == 0) {
                CSVLoader.saveProducts();
                System.out.println("Saved. Bye!");
                break;
            }


            switch (choice) {

                // Basic requirements checks
                case 1: { // Search Product by ID (BST)
                    int id = readInt("Product ID: ");
                    Product p = ProductOperations.searchById(id);
                    if (p == null) System.out.println("Not found.");
                    else System.out.println(p);
                    break;
                }

                case 2: {
                    int id = readInt("New Product ID: ");
                    String name = readLine("Name: ");
                    double price = readDouble("Price: ");
                    int stock = readInt("Stock: ");

                    boolean ok = ProductOperations.addProduct(new Product(id, name, price, stock));
                    if (!ok) {
                        System.out.println("Already exists (same ID).");
                    } else {
                        CSVLoader.saveProducts(); // يحفظ prodcuts.csv مباشرة
                        System.out.println("Saved.");
                    }
                    break;
                }


                case 3: {
                    int id = readInt("Product ID: ");
                    System.out.println("1) Update Name  2) Update Price  3) Update Stock");
                    int t = readInt("Choose: ");
                    ProductOperations ops = new ProductOperations();

                    boolean ok = false;

                    if (t == 1) {
                        String newName = readLine("New name: ");
                        ok = ops.updateName(id, newName);
                    } else if (t == 2) {
                        double newPrice = readDouble("New price: ");
                        ok = ops.updatePrice(id, newPrice);
                    } else if (t == 3) {
                        int newStock = readInt("New stock: ");
                        ok = ops.updateStock(id, newStock);
                    }

                    if (!ok) {
                        System.out.println("Not found.");
                    } else {
                        CSVLoader.saveProducts();     // <<< هذا هو المهم
                        System.out.println("Updated & Saved.");
                    }
                    break;
                }

                // Advanced Query #2
                case 4: { // List products within price range (BST rangeQuery)
                    double minP = readDouble("Min price: ");
                    double maxP = readDouble("Max price: ");
                    LinkedList<Product> list = ProductOperations.productsInPriceRange(minP, maxP);
                    printListProducts(list);
                    break;
                }

                // Advanced Query #4
                case 5: { // List customers alphabetically (BST inOrderTraversal)
                    LinkedList<Customer> list = CustomerOperations.customersSortedAlphabetically();
                    printListCustomers(list);
                    break;
                }

                // Requirement: Customer Order History
                case 6: { // CustomerOrderHistory
                    int cid = readInt("Customer ID: ");
                    List<Order> hist = CustomerOperations.CustomerOrderHistory(cid);
                    printListOrders(hist);
                    break;
                }

                // Advanced Query #1
                case 7: { // Orders between two dates (BST in-order/range)
                    int y1 = readInt("From year: ");
                    int m1 = readInt("From month: ");
                    int d1 = readInt("From day: ");

                    int y2 = readInt("To year: ");
                    int m2 = readInt("To month: ");
                    int d2 = readInt("To day: ");

                    LinkedList<Order> list = OrderOperations.ordersBetween(y1, m1, d1, y2, m2, d2);
                    printListOrders(list);
                    break;
                }

                // Advanced Query #3
                case 8: { // Top 3 highest rated (BST traversal logic)
                    LinkedList<Product> top = ProductOperations.top3HighestRatedProducts();
                    printListProducts(top);
                    break;
                }

                // Advanced Query #5 (sorted by customerId)
                case 9: {
                    int pid = readInt("Product ID: ");
                    LinkedList<Customer> list = ProductOperations.customersWhoReviewedProductByCustomerId(pid);
                    printListCustomers(list);
                    break;
                }

                // Advanced Query #5 (sorted by rating desc then customerId)
                case 10: {
                    int pid = readInt("Product ID: ");
                    LinkedList<Customer> list = ProductOperations.customersWhoReviewedProductByRating(pid);
                    printListCustomers(list);
                    break;
                }

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n==============================");
        System.out.println("1) Search Product by ID");
        System.out.println("2) Add Product");
        System.out.println("3) Update Product (name/price/stock)");
        System.out.println("4) Advanced #2: Products in Price Range");
        System.out.println("5) Advanced #4: Customers Sorted Alphabetically");
        System.out.println("6) Requirement: Customer Order History");
        System.out.println("7) Advanced #1: Orders Between Two Dates");
        System.out.println("8) Advanced #3: Top 3 Highest Rated Products");
        System.out.println("9) Advanced #5: Customers Who Reviewed Product (by customerId)");
        System.out.println("10) Advanced #5: Customers Who Reviewed Product (by rating desc)");
        System.out.println("0) Save & Exit");
        System.out.println("==============================");
    }

    // --------- printing helpers (compatible with your List/LinkedList) ---------
    private static void printListProducts(List<Product> l) {
        if (l == null || l.empty()) {
            System.out.println("No results.");
            return;
        }
        l.findFirst();
        while (true) {
            Product p = l.retrieve();
            if (p != null) System.out.println(p);
            if (l.last()) break;
            l.findNext();
        }
    }

    private static void printListCustomers(List<Customer> l) {
        if (l == null || l.empty()) {
            System.out.println("No results.");
            return;
        }
        l.findFirst();
        while (true) {
            Customer c = l.retrieve();
            if (c != null) System.out.println(c);
            if (l.last()) break;
            l.findNext();
        }
    }

    private static void printListOrders(List<Order> l) {
        if (l == null || l.empty()) {
            System.out.println("No results.");
            return;
        }
        l.findFirst();
        while (true) {
            Order o = l.retrieve();
            if (o != null) System.out.println(o);
            if (l.last()) break;
            l.findNext();
        }
    }

    // --------- input helpers (simple) ---------
    private static int readInt(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private static double readDouble(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Double.parseDouble(in.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static String readLine(String msg) {
        System.out.print(msg);
        return in.nextLine();
    }
}
