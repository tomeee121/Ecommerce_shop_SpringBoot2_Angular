package pl.project.wwsis.ecommerceshop.shop_accountsManagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.HamcrestCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.DTO.OrderHistoryDTO;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.PasswordGenerator;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.UserDetailsServiceImpl;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO.CustomerRepo;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO.Purchase;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Address;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Customer;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Order;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.OrderItem;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.service.CheckoutService;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.service.CheckoutServiceImpl;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PasswordGenerator passwordGenerator;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    void whenRegisteredNewCustomer_ThenItsBodyAndResponseOfStatus2xxObtained() throws Exception {
        //given
        Customer customer = new Customer();
        customer.setFirstName("Adam");
        customer.setLastName("Nowak");
        customer.setEmail("test@wp.pl");
        customer.setUsername("adam123");
        customer.setPassword("pass123");
        Mockito.when(passwordGenerator.generatePassword()).thenReturn("pass123");


        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/customer/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customer))).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();

        //then
        Customer customerRegistered = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Customer.class);
        Assertions.assertEquals("adam123", customerRegistered.getUsername());
        Assertions.assertEquals("test@wp.pl", customerRegistered.getEmail());
        Assertions.assertEquals(true, customerRegistered.isNotLocked());
    }

    @Test
    void whenLoggedInAsExistingCustomer_thenReceiveJWT() throws Exception {
        //given
        Customer customerRegister = new Customer();
        customerRegister.setFirstName("Adam");
        customerRegister.setLastName("Nowak");
        customerRegister.setEmail("test12345@wp.pl");
        customerRegister.setUsername("adamek1");

        Customer customerLogin = new Customer();
        customerLogin.setUsername("adamek1");
        customerLogin.setPassword("pass12345");

        Mockito.when(passwordGenerator.generatePassword()).thenReturn("pass12345");

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerRegister))).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());


        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/customer/login").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerLogin))).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String token = mvcResult.getResponse().getHeader("Jwt-token");

        //then
        Assertions.assertNotNull(token);
    }

    @Test
    void whenBoughtItemsLoggedCustomer_thenBeAbleToSeeHistoryOfOrders() throws Exception {
        //given

        Customer customer = new Customer(1L, "a1", "Adam", "Nowak", "test1234@wp.pl", "adamek123", "pass123", "/static/images/123", "USER", "user:read", true, true);
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
        order.setOrderItems(orderItems);
        order.setDateCreated(null);
        order.setDateUpdated(null);
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setBillingAddress(address);
        Purchase purchase = new Purchase(customer, order, address, address, orderItems);

        Customer customerRegister = new Customer();
        customerRegister.setFirstName("Adam");
        customerRegister.setLastName("Nowak");
        customerRegister.setEmail("test1234@wp.pl");
        customerRegister.setUsername("adamek123");

        Customer customerLogin = new Customer();
        customerLogin.setUsername("adamek123");
        customerLogin.setPassword("pass123");

        Mockito.when(passwordGenerator.generatePassword()).thenReturn("pass123");

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerRegister))).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/customer/login").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerLogin))).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String token = mvcResult.getResponse().getHeader("Jwt-token");

        MvcResult purchasePlacedMvc = mockMvc.perform(MockMvcRequestBuilders.post("/checkout/purchase").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(purchase))).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Purchase purchasePlaced = objectMapper.readValue(purchasePlacedMvc.getResponse().getContentAsString(), Purchase.class);

        //when
        MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/customer/shopping-history")
                        .param("email","test1234@wp.pl").param("pageNumber", "0").param("pageSize","1")
                        .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        OrderHistoryDTO[] ordersDTO = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), OrderHistoryDTO[].class);

        //then
        Assertions.assertNotNull(token);
        Assertions.assertNotNull(ordersDTO);
        Assertions.assertEquals(1,ordersDTO.length);
        assertThat(purchasePlaced.getOrderTrackingNumber()).hasSize(36);
    }

    @Test
    void whenSearchOrdersHistoryForGivenCustomerWithoutToken_thenExceptionHandlerIsInvoked() throws Exception {

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/customer/shopping-history")
                        .param("email", "test1234@wp.pl").param("pageNumber", "0").param("pageSize", "1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("FORBIDDEN"));

    }
}