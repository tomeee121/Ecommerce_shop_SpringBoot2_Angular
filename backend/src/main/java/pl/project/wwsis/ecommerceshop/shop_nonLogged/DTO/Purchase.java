package pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO;

import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Address;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Customer;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Order;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.OrderItem;

import java.util.Set;

public class Purchase {
    private Customer customer;
    private Order order;
    private Address shippingAddress;
    private Address billingAddress;
    private Set<OrderItem> orderItems;
    private String orderTrackingNumber;

    public Purchase() {
    }

    public Purchase(Customer customer, Order order, Address shippingAddress, Address billingAddress, Set<OrderItem> orderItems) {
        this.customer = customer;
        this.order = order;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.orderItems = orderItems;
    }

    public String getOrderTrackingNumber() {
        return orderTrackingNumber;
    }

    public void setOrderTrackingNumber(String orderTrackingNumber) {
        this.orderTrackingNumber = orderTrackingNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
