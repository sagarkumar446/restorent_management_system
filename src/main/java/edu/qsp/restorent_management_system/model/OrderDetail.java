package edu.qsp.restorent_management_system.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
@Entity
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderDetail_seq")
    @SequenceGenerator(name = "orderDetail_seq", sequenceName = "orderDetail_seq", allocationSize = 1, initialValue=100000)    
    private Long orderDetailId;
    private Integer quantity;
    private Double unitPrice;
    
    @ManyToOne
    private MenuItem MenuItem;

    // Getters and Setters
    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public MenuItem getMenuItem() {
        return MenuItem;
    }

    public void setMenuItem(MenuItem MenuItem) {
        this.MenuItem = MenuItem;
    }
}
