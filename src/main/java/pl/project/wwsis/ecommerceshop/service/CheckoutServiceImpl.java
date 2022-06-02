package pl.project.wwsis.ecommerceshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.wwsis.ecommerceshop.DAO.CustomerRepo;
import pl.project.wwsis.ecommerceshop.DTO.Purchase;
import pl.project.wwsis.ecommerceshop.DTO.PurchaseResponse;
import pl.project.wwsis.ecommerceshop.model.Customer;
import pl.project.wwsis.ecommerceshop.model.Order;
import pl.project.wwsis.ecommerceshop.model.OrderItem;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private CustomerRepo customerRepo;

    @Autowired
    public CheckoutServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        Order order = purchase.getOrder();

        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(orderItem -> order.add(orderItem));

        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        Customer customer = purchase.getCustomer();
        customer.add(order);

        customerRepo.save(customer);

        return new PurchaseResponse(orderTrackingNumber);

    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
