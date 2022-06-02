package pl.project.wwsis.ecommerceshop.DTO;

import pl.project.wwsis.ecommerceshop.model.Address;
import pl.project.wwsis.ecommerceshop.model.Customer;
import pl.project.wwsis.ecommerceshop.model.Order;
import pl.project.wwsis.ecommerceshop.model.OrderItem;

import java.util.Set;

public class Purchase {
    private Customer customer;
    private Order order;
    private Address shippingAddress;
    private Address billingAddress;
    private Set<OrderItem> orderItems;

    public Purchase(Customer customer, Order order, Address shippingAddress, Address billingAddress, Set<OrderItem> orderItems) {
        this.customer = customer;
        this.order = order;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.orderItems = orderItems;
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
