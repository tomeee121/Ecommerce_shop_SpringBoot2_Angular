package pl.project.wwsis.ecommerceshop.shop_nonLogged.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO.Purchase;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Address;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Customer;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Order;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.OrderItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenPlacingOrder_ThenSendInfoAboutOrderTrackingNrToClient() throws Exception {
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


        //when
        MvcResult mvcResult = mockMvc.perform(post("/checkout/purchase?useSSL=false&trustServerCertificate=true&allowPublicKeyRetrieval=true")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(purchase)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Purchase purchaseResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Purchase.class);

        //then
        Assertions.assertNotNull(purchaseResponse);
        assertThat(purchaseResponse.getOrderTrackingNumber(), instanceOf(String.class));
    }
}