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
import java.math.BigDecimal;
import java.util.Optional;
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
    public Purchase placeOrder(Purchase purchase) {
        BigDecimal totalPrice = purchase.getOrder().getTotalPrice();
        int totalQuantity = purchase.getOrder().getTotalQuantity();
        Order order = new Order(totalQuantity, totalPrice, "proceeded, prepare for shipment");

        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(orderItem -> order.add(orderItem));

        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        Customer customer = purchase.getCustomer();

        Optional<Customer> customerByEmail = customerRepo.findCustomerByEmail(customer.getEmail());
        if(customerByEmail.isPresent()){
            customer = customerByEmail.get();
        }

        customer.add(order);

        customerRepo.save(customer);

        System.out.println(new PurchaseResponse(orderTrackingNumber).getOrderTrackingNumber());
        Purchase responsePurchase = new Purchase();
        responsePurchase.setOrderTrackingNumber(orderTrackingNumber);

        return responsePurchase;
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
