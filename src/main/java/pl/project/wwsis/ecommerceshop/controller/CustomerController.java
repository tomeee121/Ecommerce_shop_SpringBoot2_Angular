package pl.project.wwsis.ecommerceshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.project.wwsis.ecommerceshop.constant.SecurityConstant;
import pl.project.wwsis.ecommerceshop.exception.EmailExistException;
import pl.project.wwsis.ecommerceshop.exception.ExceptionHandling;
import pl.project.wwsis.ecommerceshop.exception.UserNotFoundException;
import pl.project.wwsis.ecommerceshop.exception.UsernameExistException;
import pl.project.wwsis.ecommerceshop.model.Customer;
import pl.project.wwsis.ecommerceshop.model.CustomerPrincipal;
import pl.project.wwsis.ecommerceshop.service.CustomerService;
import pl.project.wwsis.ecommerceshop.utility.JWTTokenProvider;

@RestController
@RequestMapping(value = "/user")
public class CustomerController extends ExceptionHandling {

    private CustomerService customerService;
    private JWTTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;


    @Autowired
    public CustomerController(CustomerService customerService, JWTTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> register(@RequestBody Customer customer) throws UserNotFoundException, EmailExistException, UsernameExistException {
        Customer customerSaved = customerService.register(customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getUsername());
        return new ResponseEntity<Customer>(customerSaved, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Customer> login(@RequestBody Customer customer) throws UserNotFoundException, EmailExistException, UsernameExistException {
        authenticate(customer.getUsername(), customer.getPassword());
        Customer customerByUsername = customerService.findByUsername(customer.getUsername());
        CustomerPrincipal customerPrincipal = new CustomerPrincipal(customerByUsername);
        HttpHeaders httpHeaders = getJwtHeaders(customerPrincipal);
        return new ResponseEntity<>(customerByUsername, httpHeaders, HttpStatus.ACCEPTED);

    }

    private HttpHeaders getJwtHeaders(CustomerPrincipal customerPrincipal) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(customerPrincipal));
        return httpHeaders;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
