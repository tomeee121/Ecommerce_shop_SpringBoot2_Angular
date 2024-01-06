package pl.project.wwsis.ecommerceshop.shop_nonLogged.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO.CustomerRepo;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO.Purchase;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO.PurchaseResponse;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Customer;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Order;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.OrderItem;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
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

    @Value("${ifProdEnv}")
    private boolean ifProdEnv;

    @Override
    @Transactional
    public Purchase placeOrder(Purchase purchase) {
        BigDecimal totalPrice = purchase.getOrder().getTotalPrice();
        int totalQuantity = purchase.getOrder().getTotalQuantity();
        Order order = new Order(totalQuantity, totalPrice, "proceeded, prepare for shipment");

        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        if (ifProdEnv) {
            order.setDateCreated(new Date());
            order.setDateUpdated(new Date());
        }

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
