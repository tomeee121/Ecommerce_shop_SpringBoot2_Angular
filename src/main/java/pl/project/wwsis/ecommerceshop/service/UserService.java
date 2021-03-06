package pl.project.wwsis.ecommerceshop.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.project.wwsis.ecommerceshop.DTO.OrderHistoryDTO;
import pl.project.wwsis.ecommerceshop.exception.EmailExistException;
import pl.project.wwsis.ecommerceshop.exception.UserNotFoundException;
import pl.project.wwsis.ecommerceshop.exception.UsernameExistException;
import pl.project.wwsis.ecommerceshop.model.Customer;
import pl.project.wwsis.ecommerceshop.model.Order;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {

    Customer register(String firstName, String lastName, String email, String username) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;
    List<Customer> getCustomers();
    Customer findByUsername(String username);
    Customer findByEmail(String email);
    Customer addNewCustomer(String firstName, String lastName, String username, String email, String role, boolean isActive, boolean isNonLocked, MultipartFile profilImage) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException, IOException;
    Customer updateCustomer(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isActive, boolean isNonLocked, MultipartFile newProfilImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
    void deleteCustomer(long id);
    void resetPassword(String email) throws UserNotFoundException, MessagingException;
    Customer updateProfileImage(String username, MultipartFile picture) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
    List<OrderHistoryDTO> getShoppingHistory(String email, int page, int size);
    List<OrderHistoryDTO> getAllShoppingHistory();

    void deleteOrder(String order_tracking_number);

    void updateOrderStatus(String order_nr, String status);
}
