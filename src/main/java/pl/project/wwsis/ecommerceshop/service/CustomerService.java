package pl.project.wwsis.ecommerceshop.service;

import pl.project.wwsis.ecommerceshop.exception.EmailExistException;
import pl.project.wwsis.ecommerceshop.exception.UserNotFoundException;
import pl.project.wwsis.ecommerceshop.exception.UsernameExistException;
import pl.project.wwsis.ecommerceshop.model.Customer;

import javax.mail.MessagingException;
import java.util.List;

public interface CustomerService {

    Customer register(String firstName, String lastName, String email, String username) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;
    List<Customer> getCustomers();
    Customer findByUsername(String username);
    Customer findByEmail(String email);
}
