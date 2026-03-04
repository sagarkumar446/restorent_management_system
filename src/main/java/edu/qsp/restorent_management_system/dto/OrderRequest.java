package edu.qsp.restorent_management_system.dto;

import java.util.List;

public class OrderRequest {
    private Long customerId;
    private List<OrderItemDetail> items;
    private String orderDate;
    private String orderTime;
    private String specialInstructions;
    private PaymentDetail payment;

    public OrderRequest() {
    }

    public OrderRequest(Long customerId, List<OrderItemDetail> items, String orderDate, String orderTime, String specialInstructions) {
        this.customerId = customerId;
        this.items = items;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.specialInstructions = specialInstructions;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDetail> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDetail> items) {
        this.items = items;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public PaymentDetail getPayment() {
        return payment;
    }

    public void setPayment(PaymentDetail payment) {
        this.payment = payment;
    }

    public static class OrderItemDetail {
        private Integer menuItemId;
        private Integer quantity;

        public OrderItemDetail() {
        }

        public OrderItemDetail(Integer menuItemId, Integer quantity) {
            this.menuItemId = menuItemId;
            this.quantity = quantity;
        }

        public Integer getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(Integer menuItemId) {
            this.menuItemId = menuItemId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public static class PaymentDetail {
        private String paymentDate;
        private String paymentMethod;
        private String transactionReference;
        private Double amountPaid;

        public PaymentDetail() {
        }

        public PaymentDetail(String paymentDate, String paymentMethod, String transactionReference, Double amountPaid) {
            this.paymentDate = paymentDate;
            this.paymentMethod = paymentMethod;
            this.transactionReference = transactionReference;
            this.amountPaid = amountPaid;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getTransactionReference() {
            return transactionReference;
        }

        public void setTransactionReference(String transactionReference) {
            this.transactionReference = transactionReference;
        }

        public Double getAmountPaid() {
            return amountPaid;
        }

        public void setAmountPaid(Double amountPaid) {
            this.amountPaid = amountPaid;
        }
    }
}
