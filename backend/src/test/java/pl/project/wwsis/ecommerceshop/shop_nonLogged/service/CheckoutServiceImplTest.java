package pl.project.wwsis.ecommerceshop.shop_nonLogged.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO.CustomerRepo;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO.Purchase;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Address;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Customer;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Order;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.OrderItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    private CheckoutService checkoutService;

    @Test
    void whenOrderPlaced_shouldReturnOrderTrackingNrToTheClient() {
        //given
        Customer customer = new Customer(1L, "a1", "Adam", "Nowak", "test@wp.pl", "adam123", "pass", "/static/images/123", "USER", "user:read", true, true);
        Order order = new Order();
        OrderItem orderItem = new OrderItem("/static/images/product1", BigDecimal.valueOf(40.00), 2, 1L, order);
        OrderItem orderItem2 = new OrderItem("/static/images/product2", BigDecimal.valueOf(59.10), 2, 2L, order);
        OrderItem orderItem3 = new OrderItem("/static/images/product3", BigDecimal.valueOf(29.00), 2, 3L, order);
        Set<OrderItem> orderItems = Set.of(orderItem, orderItem2, orderItem3);

        Address address = new Address("Poland", "47-400", "Racibórz", "śląskie", "Raciborska", order);
//        order.setId(1L);
//        order.setOrderTrackingNumber("track123");
        order.setTotalQuantity(5);
        order.setTotalPrice(BigDecimal.valueOf(300.10));
//        order.setStatus("received");
//        order.setDateCreated(new Date());
//        order.setOrderItems(orderItems);
        order.setCustomer(customer);
//        order.setShippingAddress(address);
//        order.setBillingAddress(address);
        Purchase purchase = new Purchase(customer, order, address, address, orderItems);
        CustomerRepo repoMocked = Mockito.mock(CustomerRepo.class);
        checkoutService = new CheckoutServiceImpl(repoMocked);

        //when
        Purchase purchaseProcessed = checkoutService.placeOrder(purchase);

        //then
        assertNotNull(purchaseProcessed.getOrderTrackingNumber());
    }


    @Test
    void whenPurchaseDTOPassedToService_shouldSaveCustomerObjectToDatabase() {
        //given
        Customer customer = new Customer(1L, "a1", "Adam", "Nowak", "test@wp.pl", "adam123", "pass", "/static/images/123", "USER", "user:read", true, true);
        Order order = new Order();
        OrderItem orderItem = new OrderItem("/static/images/product1", BigDecimal.valueOf(40.00), 2, 1L, order);
        OrderItem orderItem2 = new OrderItem("/static/images/product2", BigDecimal.valueOf(59.10), 2, 2L, order);
        OrderItem orderItem3 = new OrderItem("/static/images/product3", BigDecimal.valueOf(29.00), 2, 3L, order);
        Set<OrderItem> orderItems = Set.of(orderItem, orderItem2, orderItem3);

        Address address = new Address("Poland", "47-400", "Racibórz", "śląskie", "Raciborska", order);
        order.setId(1L);
        order.setOrderTrackingNumber("track123");
        order.setTotalQuantity(5);
        order.setTotalPrice(BigDecimal.valueOf(300.10));
        order.setStatus("received");
        order.setDateCreated(new Date());
        order.setOrderItems(orderItems);
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setBillingAddress(address);
        Purchase purchase = new Purchase(customer, order, address, address, orderItems);

        CustomerRepo customerRepoMock = Mockito.mock(CustomerRepo.class);
        checkoutService = new CheckoutServiceImpl(customerRepoMock);
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        //when
        Purchase purchaseProcessed = checkoutService.placeOrder(purchase);

        //then
        verify(customerRepoMock).save(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(1, captor.getValue().getOrders().size());
    }
}