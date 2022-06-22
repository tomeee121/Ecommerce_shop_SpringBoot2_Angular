package pl.project.wwsis.ecommerceshop.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.project.wwsis.ecommerceshop.DAO.CustomerRepo;
import pl.project.wwsis.ecommerceshop.enums.Role;
import pl.project.wwsis.ecommerceshop.exception.EmailExistException;
import pl.project.wwsis.ecommerceshop.exception.UserNotFoundException;
import pl.project.wwsis.ecommerceshop.exception.UsernameExistException;
import pl.project.wwsis.ecommerceshop.model.Customer;
import pl.project.wwsis.ecommerceshop.model.CustomerPrincipal;

import java.util.List;
import java.util.Optional;

import static pl.project.wwsis.ecommerceshop.constant.UserServiceImpConstants.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, CustomerService {

    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private CustomerRepo customerRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetailsServiceImpl(CustomerRepo customerRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepo = customerRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null){
            logger.error("no username given");
            throw new UsernameNotFoundException("User not found by username "+username);
        }
        Optional<Customer> customer = customerRepo.findCustomerByUsername(username);
        CustomerPrincipal customerPrincipal = null;
        if(customer.isPresent()){
            customerPrincipal = new CustomerPrincipal(customer.get());
        }
        return customerPrincipal;
    }

    @Override
    public Customer register(String firstName, String lastName, String email, String username) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndPassword(StringUtils.EMPTY, username, email);
        Customer customer = new Customer();
        customer.setCustomerId(generateCustomerId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        customer.setPassword(encodedPassword);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setUsername(username);
        customer.setPassword(encodedPassword);
        customer.setActive(true);
        customer.setNotLocked(true);
        customer.setRole(Role.ROLE_USER.name());
        customer.setAuthorities(Role.ROLE_USER.getAuthorities());
        customer.setImageUrl(getTemporaryImageUrl());
        customerRepo.save(customer);
        logger.info("password is: "+ password);
        return customer;
    }

    private String getTemporaryImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PROFILE_TEMP).toUriString();
    }

    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String generateCustomerId() {
        return RandomStringUtils.random(10);
    }

    private Customer validateNewUsernameAndPassword(String currentUsername, String newUsername, String email) throws UserNotFoundException, UsernameExistException, EmailExistException {
        Customer customer = null;
        if (StringUtils.isNotBlank(currentUsername)) {
            customer = customerRepo.findCustomerByUsername(currentUsername)
                    .orElseThrow(() -> new UserNotFoundException("User not found by username: " + currentUsername));

            Optional<Customer> customerByNewUsername = customerRepo.findCustomerByUsername(newUsername);
            if (customerByNewUsername.isPresent()) {
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }
            Optional<Customer> customerByNewEmail = customerRepo.findCustomerByEmail(email);
            if (customerByNewEmail.isPresent()) {
                throw new EmailExistException("Email already taken!");
            }
            return customer;
        }
        else{
            Optional<Customer> customerByUsername = customerRepo.findCustomerByUsername(newUsername);
            if (customerByUsername.isPresent()) {
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }
            Optional<Customer> customerByEmail = customerRepo.findCustomerByEmail(email);
            if (customerByEmail.isPresent()) {
                throw new EmailExistException(EMAIL_ALREADY_TAKEN);
            }
            return null;
        }



    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepo.findAll();
    }

    @Override
    public Customer findByUsername(String username) {
        Optional<Customer> customerByUsername = customerRepo.findCustomerByUsername(username);
        if(customerByUsername.isPresent()){
            return customerByUsername.get();
        }
        return null;
    }

    @Override
    public Customer findByEmail(String email) {
        Optional<Customer> customerByEmail = customerRepo.findCustomerByEmail(email);
        if(customerByEmail.isPresent()){
            return customerByEmail.get();
        }
        return null;

    }
}
