package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.DTO.OrderHistoryDTO;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.FileConstant;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.enums.Role;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.exception.EmailExistException;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.exception.UserNotFoundException;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.exception.UsernameExistException;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.model.CustomerPrincipal;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.MailSenderBeanImpls.*;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO.CustomerRepo;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO.OrderItemRepo;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO.OrderRepo;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Customer;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Order;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.OrderItem;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.FileConstant.*;
import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.UserServiceImpConstants.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private CustomerRepo customerRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private LoginAttemptService loginAttemptService;
    private MailSenderBeanForNormalUser mailSenderForNormalUser;
    private MailSenderBeanForHR mailSenderForHR;
    private MailSenderBeanForManager mailSenderForManager;
    private MailSenderBeanForSuperAdmin mailSenderForSuperAdmin;
    private MailSenderBeanForAdmin mailSenderForAdmin;
    private OrderRepo orderRepo;
    private PasswordGenerator passwordGenerator;

    @Autowired
    Environment env;
    @Autowired
    private OrderItemRepo orderItemRepo;

    public UserDetailsServiceImpl(CustomerRepo customerRepo, BCryptPasswordEncoder bCryptPasswordEncoder, LoginAttemptService loginAttemptService
            , MailSenderBeanForNormalUser mailSenderForNormalUser, MailSenderBeanForHR mailSenderForHR, MailSenderBeanForManager mailSenderForManager
            , MailSenderBeanForSuperAdmin mailSenderForSuperAdmin, MailSenderBeanForAdmin mailSenderForAdmin, OrderRepo orderRepo, PasswordGenerator passwordGenerator) {
        this.customerRepo = customerRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.mailSenderForNormalUser = mailSenderForNormalUser;
        this.mailSenderForHR = mailSenderForHR;
        this.mailSenderForManager = mailSenderForManager;
        this.mailSenderForSuperAdmin = mailSenderForSuperAdmin;
        this.mailSenderForAdmin = mailSenderForAdmin;
        this.orderRepo = orderRepo;
        this.passwordGenerator = passwordGenerator;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepo.findCustomerByUsername(username);
        if(!customer.isPresent()){
            logger.error("no username given");
            throw new UsernameNotFoundException("User not found by username "+username);
        }
        validateLoginAttempt(customer.get());
        CustomerPrincipal customerPrincipal = null;
        if(customer.isPresent()){
            customerPrincipal = new CustomerPrincipal(customer.get());
        }
        return customerPrincipal;
    }

    private void validateLoginAttempt(Customer customer) {
        if (customer.isNotLocked()) {
            if (loginAttemptService.ifHasExceededMaxAmountOfLoginAttemps(customer.getUsername())) {
                customer.setNotLocked(false);
            } else {
                customer.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(customer.getUsername());
        }
    }

    @Override
    public Customer register(String firstName, String lastName, String email, String username, String password) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {

        validateNewUsernameAndPassword(StringUtils.EMPTY, username, email);
        Customer customer = new Customer();
        customer.setCustomerId(generateCustomerId());
//        String password = passwordGenerator.generatePassword();
//        String password = "zYoiOp1;";
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
        customer.setImageUrl(getTemporaryImageUrl(username));
        customerRepo.save(customer);
        System.out.println(customer);
//        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
//            mailSenderForNormalUser.sendEmail(firstName, password, email);
//        }
        customer.setPassword(null);
        return customer;
    }

    @Override
    public Customer addNewCustomer(String firstName, String lastName, String username, String email, String role, boolean isActive, boolean isNonLocked, MultipartFile profilImage) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException, IOException {
        validateNewUsernameAndPassword(StringUtils.EMPTY, username,email);
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
        customer.setActive(isActive);
        customer.setNotLocked(isNonLocked);
        customer.setRole(getEnumRoleName(role));
        customer.setAuthorities(getEnumRoleAuthorities(role));
        if(profilImage == null){
        customer.setImageUrl(getTemporaryImageUrl(username));}
        else{
            customer.setImageUrl(getImageUrl(username));
        }
        customerRepo.save(customer);

        customizedEmailSender(role, password, email, firstName);
        saveProfileImage(customer, profilImage);
        return customer;
    }

    public void customizedEmailSender(String role, String password, String email, String firstName) throws MessagingException {
        switch (role){
            case "ROLE_USER": mailSenderForNormalUser.sendEmail(firstName, password, email);
            break;
            case "ROLE_ADMIN": mailSenderForAdmin.sendEmail(firstName, password, email);
            break;
            case "ROLE_MANAGER": mailSenderForManager.sendEmail(firstName, password, email);
            break;
            case "ROLE_SUPER_ADMIN": mailSenderForSuperAdmin.sendEmail(firstName, password, email);
            break;
            case "ROLE_HR": mailSenderForHR.sendEmail(firstName, password, email);
            break;
        }
    }

    @Override
    public Customer updateCustomer(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isActive, boolean isNonLocked, MultipartFile newProfilImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException {
        Customer currentCcustomer = validateNewUsernameAndPassword(currentUsername, newUsername, newEmail);
        currentCcustomer.setFirstName(newFirstName);
        currentCcustomer.setLastName(newLastName);
        currentCcustomer.setEmail(newEmail);
        currentCcustomer.setUsername(newUsername);
        currentCcustomer.setActive(isActive);
        currentCcustomer.setNotLocked(isNonLocked);
        currentCcustomer.setRole(getEnumRoleName(newRole));
        currentCcustomer.setAuthorities(getEnumRoleAuthorities(newRole));
        customerRepo.save(currentCcustomer);
        saveProfileImage(currentCcustomer, newProfilImage);
        return currentCcustomer;
    }

    private String getEnumRoleName(String role) {
        return Role.valueOf(role).name();
    }

    private String getEnumRoleAuthorities(String role){
        return Role.valueOf(role).getAuthorities();
    }

    @Override
    public void deleteCustomer(long id) {
        customerRepo.deleteById(id);
    }

    @Override
    public void resetPassword(String email) throws UserNotFoundException, MessagingException {
        Optional<Customer> customerByEmail = customerRepo.findCustomerByEmail(email);
        if(!customerByEmail.isPresent()){
            throw new UserNotFoundException(NO_SUCH_EMAIL+email);
        }
        else{
            String newPassword = generatePassword();
            String encodedNewPassword = encodePassword(newPassword);
            customerByEmail.get().setPassword(encodedNewPassword);
            customerRepo.save(customerByEmail.get());
            mailSenderForNormalUser.sendNewPasswordEmail(customerByEmail.get().getFirstName(), newPassword, customerByEmail.get().getEmail());

        }
    }

    private void saveProfileImage(Customer customer, MultipartFile profilImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException {
        Path userFolder = Paths.get(USER_FOLDER).resolve(customer.getUsername()).toAbsolutePath().normalize();
        if (profilImage != null) {
            if (!Files.exists(userFolder)) {
                new File(String.valueOf(userFolder)).mkdirs();
                Files.copy(profilImage.getInputStream(), userFolder
                        .resolve(customer.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            } else {
                Files.delete(Paths.get(String.valueOf(userFolder.resolve(customer.getUsername() + DOT + JPG_EXTENSION))));
                Files.copy(profilImage.getInputStream(), userFolder
                        .resolve(customer.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);

            }
        }
        customer.setImageUrl(getImageUrl(customer.getUsername()));
        customerRepo.save(customer);
    }

    private String getImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path((USER_IMAGE_PATH + FORWARD_SLASH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION)).toUriString();
    }

    @Override
    public Customer updateProfileImage(String username, MultipartFile picture) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException {
        Customer customerValidated = validateNewUsernameAndPassword(username, StringUtils.EMPTY, StringUtils.EMPTY);
        saveProfileImage(customerValidated, picture);
        return customerValidated;
    }

    @Override
    public List<OrderHistoryDTO> getShoppingHistory(String email, int page, int size) {
        List<Order> ordersByEmail = orderRepo.findOrdersByEmail(email, PageRequest.of(page, size));
        List<OrderHistoryDTO> orderHistoryDTOS = mapOrderToDTO(ordersByEmail);
        System.out.println(orderHistoryDTOS +"  <-- list DTO");
        return orderHistoryDTOS;
    }

    @Override
    public List<OrderHistoryDTO> getAllShoppingHistory() {
        List<Order> allOrders = orderRepo.findAllOrders();
        List<OrderHistoryDTO> orderHistoryDTOS = mapOrderToDTO(allOrders);
        return orderHistoryDTOS;

    }


    @Transactional
    @Override
    public void deleteOrder(String order_tracking_number) {
        Order order = orderRepo.findByOrderTrackingNumber(order_tracking_number).orElseThrow(() -> new IllegalArgumentException("No order with such tracking nr"));
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(null);
            order.setOrderItems(null);
            orderRepo.save(order);
            orderItemRepo.save(orderItem);
            orderItemRepo.deleteById(orderItem.getId());
        }
        orderRepo.deleteOrderByOrderTracking(order_tracking_number);
    }

    @Transactional
    @Override
    public void updateOrderStatus(String order_nr, String status) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime myDateObj = LocalDateTime.now();
        System.out.println("Before formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);
        System.out.println("after: "+formattedDate);
        orderRepo.updateOrderStatus(status, order_nr, new Date());
    }

    @Transactional
    void saveCustomerWithNewOrders(Customer customerByOrderTrackingNumber, Set<Order> orders){
        Customer customer = new Customer(customerByOrderTrackingNumber.getId(), customerByOrderTrackingNumber.getCustomerId(), customerByOrderTrackingNumber.getFirstName(), customerByOrderTrackingNumber.getLastName()
                , customerByOrderTrackingNumber.getEmail(), customerByOrderTrackingNumber.getUsername(), customerByOrderTrackingNumber.getPassword(), customerByOrderTrackingNumber.getImageUrl(),
                customerByOrderTrackingNumber.getRole(), customerByOrderTrackingNumber.getAuthorities(), customerByOrderTrackingNumber.isActive(),
                customerByOrderTrackingNumber.isNotLocked());
        customer.setOrders(orders);
        customerRepo.save(customerByOrderTrackingNumber);
    }

    private List<OrderHistoryDTO> mapOrderToDTO(List<Order> ordersByEmail) {
        List<OrderHistoryDTO> ordersByEmailDTO = ordersByEmail.stream().map(order -> new OrderHistoryDTO(order.getOrderTrackingNumber(), order.getTotalQuantity(),
                        order.getStatus(), order.getDateCreated(), order.getDateUpdated(), order.getShippingAddress().getCountry(),
                        order.getShippingAddress().getZipCode(), order.getShippingAddress().getCity(), order.getShippingAddress().getState(),
                        order.getShippingAddress().getStreet()))
                        .collect(Collectors.toList());
        return ordersByEmailDTO;
    }

    private String getTemporaryImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.DEFAULT_USER_IMAGE_PATH+username).toUriString();
    }

    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String generateCustomerId() {
        return UUID.randomUUID().toString();
    }

    private Customer validateNewUsernameAndPassword(String currentUsername, String newUsername, String email) throws UserNotFoundException, UsernameExistException, EmailExistException {
        Customer customer = null;
        if (StringUtils.isNotBlank(currentUsername)) {
            customer = customerRepo.findCustomerByUsername(currentUsername)
                    .orElseThrow(() -> new UserNotFoundException(USERNAME_ALREADY_EXISTS + currentUsername));

            Optional<Customer> customerByNewUsername = customerRepo.findCustomerByUsername(newUsername);
            if (customerByNewUsername.isPresent()) {
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }
            Optional<Customer> customerByNewEmail = customerRepo.findCustomerByEmail(email);
            if (customerByNewEmail.isPresent()) {
                throw new EmailExistException(EMAIL_ALREADY_TAKEN);
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
        return customerRepo.getCustomersCustomized();
    }

    @Override
    public Customer findByUsername(String username) {
        Optional<Customer> customerByUsername = customerRepo.findCustomerByUsername(username);
        if(customerByUsername.isPresent()){
            Customer customer = new Customer(customerByUsername.get().getId(), customerByUsername.get().getCustomerId(), customerByUsername.get().getFirstName(),
                    customerByUsername.get().getLastName(), customerByUsername.get().getEmail(), customerByUsername.get().getUsername(),
                    customerByUsername.get().getPassword(), customerByUsername.get().getImageUrl(),
                    customerByUsername.get().getRole(), customerByUsername.get().getAuthorities(),
                    customerByUsername.get().isActive(), customerByUsername.get().isNotLocked());
            return customer;
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
