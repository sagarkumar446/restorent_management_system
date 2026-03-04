package edu.qsp.restorent_management_system.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.OrderBy;
import edu.qsp.restorent_management_system.model.OrderDetail;
import edu.qsp.restorent_management_system.model.Payment;
import edu.qsp.restorent_management_system.repository.CustomerRepository;
import edu.qsp.restorent_management_system.repository.MenuRepository;
import edu.qsp.restorent_management_system.repository.OrderRepository;

@Service
public class AdminDashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MenuRepository menuRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Counts
        long totalCustomers = customerRepository.count();
        long totalMenuItems = menuRepository.count();
        long totalOrders = orderRepository.count();

        // Total revenue
        List<OrderBy> allOrders = orderRepository.findAll();
        double totalRevenue = allOrders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .mapToDouble(OrderBy::getTotalAmount)
                .sum();

        stats.put("totalRevenue", totalRevenue);
        stats.put("totalOrders", totalOrders);
        stats.put("totalCustomers", totalCustomers);
        stats.put("totalMenuItems", totalMenuItems);

        // Recent orders (last 10)
        List<OrderBy> recentOrders = orderRepository.findTop10ByOrderByOrderIdDesc();
        List<Map<String, Object>> recentActivity = new ArrayList<>();

        // Build a map of orderId -> customerName for quick lookup
        List<Customer> customers = customerRepository.findAll();
        Map<Long, String> orderToCustomer = new HashMap<>();
        for (Customer c : customers) {
            if (c.getOrders() != null) {
                for (OrderBy o : c.getOrders()) {
                    orderToCustomer.put(o.getOrderId(), c.getName());
                }
            }
        }

        for (OrderBy order : recentOrders) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("orderId", order.getOrderId());
            entry.put("orderDate", order.getOrderDate());
            entry.put("orderTime", order.getOrderTime());
            entry.put("totalAmount", order.getTotalAmount());
            entry.put("itemCount", order.getOrderDetails() != null ? order.getOrderDetails().size() : 0);
            entry.put("customerName", orderToCustomer.getOrDefault(order.getOrderId(), "Guest"));
            recentActivity.add(entry);
        }

        stats.put("recentOrders", recentActivity);

        return stats;
    }

    public List<Map<String, Object>> getAllCustomersWithHistory() {
        List<Customer> customers = customerRepository.findAll();
        customers.sort(Comparator.comparing(Customer::getCustomerId, Comparator.nullsLast(Long::compareTo)).reversed());

        List<Map<String, Object>> customerHistory = new ArrayList<>();

        for (Customer customer : customers) {
            Map<String, Object> customerData = new HashMap<>();
            customerData.put("customerId", customer.getCustomerId());
            customerData.put("name", customer.getName());
            customerData.put("email", customer.getEmail());
            customerData.put("contactNumber", customer.getContactNumber());
            customerData.put("address", customer.getAddress());
            customerData.put("registrationDate", customer.getRegistrationDate());
            customerData.put("lastLoginDate", customer.getLastLoginDate());
            customerData.put("isActive", customer.getIsActive());
            customerData.put("loyaltyPoints", customer.getLoyaltyPoints());

            List<OrderBy> orders = new ArrayList<>();
            if (customer.getOrders() != null) {
                orders.addAll(customer.getOrders());
            }
            orders.sort(Comparator.comparing(OrderBy::getOrderId, Comparator.nullsLast(Long::compareTo)).reversed());

            List<Map<String, Object>> orderHistory = new ArrayList<>();
            List<Map<String, Object>> transactionHistory = new ArrayList<>();

            double totalSpent = 0.0;
            int totalTransactions = 0;
            double totalPaidAmount = 0.0;

            for (OrderBy order : orders) {
                double orderAmount = order.getTotalAmount() != null ? order.getTotalAmount() : 0.0;
                totalSpent += orderAmount;

                List<Map<String, Object>> itemDetails = new ArrayList<>();
                int totalItemQuantity = 0;

                if (order.getOrderDetails() != null) {
                    for (OrderDetail detail : order.getOrderDetails()) {
                        int quantity = detail.getQuantity() != null ? detail.getQuantity() : 0;
                        double unitPrice = detail.getUnitPrice() != null ? detail.getUnitPrice() : 0.0;
                        double lineTotal = quantity * unitPrice;
                        totalItemQuantity += quantity;

                        Map<String, Object> itemData = new HashMap<>();
                        itemData.put("orderDetailId", detail.getOrderDetailId());
                        itemData.put("menuItemId", detail.getMenuItem() != null ? detail.getMenuItem().getMenuItemId() : null);
                        itemData.put("itemName", detail.getMenuItem() != null ? detail.getMenuItem().getItemName() : "Unknown Item");
                        itemData.put("category", detail.getMenuItem() != null ? detail.getMenuItem().getCategory() : null);
                        itemData.put("veg", detail.getMenuItem() != null ? detail.getMenuItem().getVeg() : null);
                        itemData.put("quantity", quantity);
                        itemData.put("unitPrice", unitPrice);
                        itemData.put("lineTotal", lineTotal);
                        itemDetails.add(itemData);
                    }
                }

                int itemCount = totalItemQuantity;
                if (itemCount == 0 && order.getOrderDetails() != null) {
                    itemCount = order.getOrderDetails().size();
                }

                Payment payment = order.getPayements();
                Map<String, Object> paymentData = null;
                String transactionStatus = "NOT_RECORDED";
                String paymentMethod = "N/A";
                String paymentDate = order.getOrderDate();
                double transactionAmount = orderAmount;

                if (payment != null) {
                    totalTransactions++;
                    transactionStatus = "PAID";
                    paymentMethod = payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "UNKNOWN";
                    paymentDate = payment.getPaymentDate() != null ? payment.getPaymentDate() : order.getOrderDate();
                    transactionAmount = payment.getAmountPaid() != null ? payment.getAmountPaid() : orderAmount;
                    totalPaidAmount += transactionAmount;

                    paymentData = new HashMap<>();
                    paymentData.put("paymentId", payment.getPaymentId());
                    paymentData.put("paymentDate", paymentDate);
                    paymentData.put("paymentMethod", paymentMethod);
                    paymentData.put("transactionReference", payment.getTransactionReference());
                    paymentData.put("amountPaid", transactionAmount);
                }

                Map<String, Object> orderData = new HashMap<>();
                orderData.put("orderId", order.getOrderId());
                orderData.put("orderDate", order.getOrderDate());
                orderData.put("orderTime", order.getOrderTime());
                orderData.put("totalAmount", orderAmount);
                orderData.put("itemCount", itemCount);
                orderData.put("items", itemDetails);
                orderData.put("payment", paymentData);
                orderData.put("transactionStatus", transactionStatus);
                orderHistory.add(orderData);

                Map<String, Object> transactionData = new HashMap<>();
                transactionData.put("orderId", order.getOrderId());
                transactionData.put("transactionId", payment != null ? payment.getPaymentId() : null);
                transactionData.put("transactionReference", payment != null ? payment.getTransactionReference() : null);
                transactionData.put("transactionDate", paymentDate);
                transactionData.put("transactionTime", order.getOrderTime());
                transactionData.put("paymentMethod", paymentMethod);
                transactionData.put("amount", transactionAmount);
                transactionData.put("status", transactionStatus);
                transactionHistory.add(transactionData);
            }

            customerData.put("totalOrders", orders.size());
            customerData.put("totalSpent", totalSpent);
            customerData.put("totalTransactions", totalTransactions);
            customerData.put("totalPaidAmount", totalPaidAmount);
            customerData.put("orderHistory", orderHistory);
            customerData.put("transactionHistory", transactionHistory);

            customerHistory.add(customerData);
        }

        return customerHistory;
    }
}
