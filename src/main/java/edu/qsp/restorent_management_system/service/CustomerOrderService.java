package edu.qsp.restorent_management_system.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.dto.OrderRequest;
import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.model.OrderBy;
import edu.qsp.restorent_management_system.model.OrderDetail;
import edu.qsp.restorent_management_system.model.Payment;
import edu.qsp.restorent_management_system.repository.CustomerRepository;
import edu.qsp.restorent_management_system.repository.MenuRepository;
import edu.qsp.restorent_management_system.repository.OrderDetailRepository;
import edu.qsp.restorent_management_system.repository.OrderRepository;
import edu.qsp.restorent_management_system.repository.PaymentRepository;

@Service
public class CustomerOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Place a new order
     */
    public OrderBy placeOrder(OrderRequest orderRequest) throws Exception {
        // Validate customer exists
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(orderRequest.getCustomerId());
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        // Validate order items
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new Exception("Order must contain at least one item");
        }

        // Create new order
        OrderBy newOrder = new OrderBy();
        newOrder.setOrderDate(orderRequest.getOrderDate());
        newOrder.setOrderTime(orderRequest.getOrderTime());

        // Calculate total amount and create order details
        double totalAmount = 0.0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderRequest.OrderItemDetail itemDetail : orderRequest.getItems()) {
            Optional<MenuItem> optionalMenuItem = menuRepository.findById(itemDetail.getMenuItemId().longValue());
            if (!optionalMenuItem.isPresent()) {
                throw new Exception("Menu item not found: " + itemDetail.getMenuItemId());
            }

            MenuItem menuItem = optionalMenuItem.get();
            double itemTotal = menuItem.getPrice() * itemDetail.getQuantity();
            totalAmount += itemTotal;

            // Create order detail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(itemDetail.getQuantity());
            orderDetail.setUnitPrice(menuItem.getPrice());
            orderDetail.setMenuItem(menuItem);
            orderDetails.add(orderDetail);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setOrderDetails(orderDetails);

        // Save payment details if provided
        if (orderRequest.getPayment() != null) {
            Payment payment = new Payment();
            payment.setPaymentDate(
                    orderRequest.getPayment().getPaymentDate() != null
                            ? orderRequest.getPayment().getPaymentDate()
                            : orderRequest.getOrderDate());
            payment.setPaymentMethod(
                    orderRequest.getPayment().getPaymentMethod() != null
                            ? orderRequest.getPayment().getPaymentMethod()
                            : "UNKNOWN");
            payment.setTransactionReference(orderRequest.getPayment().getTransactionReference());
            payment.setAmountPaid(
                    orderRequest.getPayment().getAmountPaid() != null
                            ? orderRequest.getPayment().getAmountPaid()
                            : totalAmount);
            Payment savedPayment = paymentRepository.save(payment);
            newOrder.setPayements(savedPayment);
        }

        // Save order details first
        for (OrderDetail detail : orderDetails) {
            orderDetailRepository.save(detail);
        }

        // Save order
        OrderBy savedOrder = orderRepository.save(newOrder);

        // Add order to customer's order list
        if (customer.getOrders() == null) {
            customer.setOrders(new ArrayList<>());
        }
        customer.getOrders().add(savedOrder);
        customerRepository.save(customer);

        // Add loyalty points (1 point per rupee spent)
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + totalAmount);
        customerRepository.save(customer);

        return savedOrder;
    }

    /**
     * Get all orders for a customer
     */
    public List<OrderBy> getCustomerOrders(Long customerId) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        return orderRepository.findByCustomerId(customerId);
    }

    /**
     * Get optimized order summary for customer (without heavy blob fields)
     */
    public List<Map<String, Object>> getCustomerOrderSummaries(Long customerId) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        List<OrderBy> orders = new ArrayList<>(orderRepository.findByCustomerId(customerId));
        orders.sort(Comparator.comparing(OrderBy::getOrderId, Comparator.nullsLast(Long::compareTo)).reversed());

        List<Map<String, Object>> summaries = new ArrayList<>();
        for (OrderBy order : orders) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", order.getOrderId());
            orderData.put("orderDate", order.getOrderDate());
            orderData.put("orderTime", order.getOrderTime());
            orderData.put("totalAmount", order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);

            int itemCount = 0;
            List<Map<String, Object>> items = new ArrayList<>();
            if (order.getOrderDetails() != null) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    int quantity = detail.getQuantity() != null ? detail.getQuantity() : 0;
                    double unitPrice = detail.getUnitPrice() != null ? detail.getUnitPrice() : 0.0;
                    double lineTotal = quantity * unitPrice;
                    itemCount += quantity;

                    Map<String, Object> item = new HashMap<>();
                    item.put("orderDetailId", detail.getOrderDetailId());
                    item.put("menuItemId", detail.getMenuItem() != null ? detail.getMenuItem().getMenuItemId() : null);
                    item.put("itemName", detail.getMenuItem() != null ? detail.getMenuItem().getItemName() : "Unknown Item");
                    item.put("category", detail.getMenuItem() != null ? detail.getMenuItem().getCategory() : null);
                    item.put("veg", detail.getMenuItem() != null ? detail.getMenuItem().getVeg() : null);
                    item.put("quantity", quantity);
                    item.put("unitPrice", unitPrice);
                    item.put("lineTotal", lineTotal);
                    items.add(item);
                }
            }
            if (itemCount == 0 && order.getOrderDetails() != null) {
                itemCount = order.getOrderDetails().size();
            }
            orderData.put("itemCount", itemCount);
            orderData.put("items", items);

            Payment payment = order.getPayements();
            Map<String, Object> paymentData = null;
            String transactionStatus = "NOT_RECORDED";
            if (payment != null) {
                transactionStatus = "PAID";
                paymentData = new HashMap<>();
                paymentData.put("paymentId", payment.getPaymentId());
                paymentData.put("paymentDate", payment.getPaymentDate());
                paymentData.put("paymentMethod", payment.getPaymentMethod());
                paymentData.put("transactionReference", payment.getTransactionReference());
                paymentData.put("amountPaid", payment.getAmountPaid());
            }
            orderData.put("payment", paymentData);
            orderData.put("transactionStatus", transactionStatus);

            summaries.add(orderData);
        }

        return summaries;
    }

    /**
     * Get specific order details
     */
    public OrderBy getOrderDetails(Long orderId) throws Exception {
        Optional<OrderBy> optionalOrder = orderRepository.findByOrderId(orderId);
        if (!optionalOrder.isPresent()) {
            throw new Exception("Order not found");
        }

        return optionalOrder.get();
    }

    /**
     * Cancel an order (only if not yet prepared)
     */
    public boolean cancelOrder(Long orderId) throws Exception {
        Optional<OrderBy> optionalOrder = orderRepository.findByOrderId(orderId);
        if (!optionalOrder.isPresent()) {
            throw new Exception("Order not found");
        }

        OrderBy order = optionalOrder.get();
        // Add more validation logic as needed (e.g., check order status)
        orderRepository.delete(order);
        return true;
    }

    /**
     * Get order history for a customer with pagination
     */
    public List<OrderBy> getOrderHistory(Long customerId, int page, int size) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        List<OrderBy> allOrders = orderRepository.findByCustomerId(customerId);

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allOrders.size());

        if (startIndex >= allOrders.size()) {
            return new ArrayList<>();
        }

        return allOrders.subList(startIndex, endIndex);
    }

    /**
     * Get total spending for a customer
     */
    public double getTotalSpending(Long customerId) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        List<OrderBy> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream().mapToDouble(OrderBy::getTotalAmount).sum();
    }

    /**
     * Get popular menu items
     */
    public List<MenuItem> getPopularMenuItems() {
        return menuRepository.findAll(); // Can be enhanced with popularity logic
    }
}
