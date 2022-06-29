package pl.project.wwsis.ecommerceshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.project.wwsis.ecommerceshop.DTO.HttpResponse;
import pl.project.wwsis.ecommerceshop.constant.SecurityConstant;
import pl.project.wwsis.ecommerceshop.exception.EmailExistException;
import pl.project.wwsis.ecommerceshop.exception.ExceptionHandling;
import pl.project.wwsis.ecommerceshop.exception.UserNotFoundException;
import pl.project.wwsis.ecommerceshop.exception.UsernameExistException;
import pl.project.wwsis.ecommerceshop.model.Customer;
import pl.project.wwsis.ecommerceshop.model.CustomerPrincipal;
import pl.project.wwsis.ecommerceshop.service.UserService;
import pl.project.wwsis.ecommerceshop.utility.JWTTokenProvider;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static pl.project.wwsis.ecommerceshop.constant.EmailConstant.CUSTOMER_DELETED_MESSAGE;
import static pl.project.wwsis.ecommerceshop.constant.EmailConstant.EMAIL_SENT;
import static pl.project.wwsis.ecommerceshop.constant.FileConstant.*;
import static pl.project.wwsis.ecommerceshop.constant.SecurityConstant.TOKEN_PREFIX;

@RestController
@RequestMapping("/customer")
public class CustomerController extends ExceptionHandling {

    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;


    @Autowired
    public CustomerController(UserService userService, JWTTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> register(@RequestBody Customer customer) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        Customer customerSaved = userService.register(customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getUsername());
        return new ResponseEntity<Customer>(customerSaved, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Customer> login(@RequestBody Customer customer) throws UserNotFoundException, EmailExistException, UsernameExistException {
        authenticate(customer.getUsername(), customer.getPassword());
        Customer customerByUsername = userService.findByUsername(customer.getUsername());
        CustomerPrincipal customerPrincipal = new CustomerPrincipal(customerByUsername);
        HttpHeaders httpHeaders = getJwtHeaders(customerPrincipal);
        return new ResponseEntity<>(customerByUsername, httpHeaders, HttpStatus.ACCEPTED);

    }

    @PostMapping("/add")
    public ResponseEntity<Customer> addNewCustomer(@RequestParam("firstName") String firstName,
                                                   @RequestParam("lastName") String lastName,
                                                   @RequestParam("username") String username,
                                                   @RequestParam("email") String email,
                                                   @RequestParam("role") String role,
                                                   @RequestParam("isActive") String isActive,
                                                   @RequestParam("isNonLocked") String isNonLocked,
                                                   @RequestParam(value = "profileImage", required = false) MultipartFile profilImage) throws UserNotFoundException, EmailExistException, MessagingException, IOException, UsernameExistException {
        Customer customerAdded = userService.addNewCustomer(firstName, lastName, username, email, role, Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNonLocked), profilImage);
        return new ResponseEntity<Customer>(customerAdded, OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Customer> updateCustomer(@RequestParam("currentUsername") String currentUsername,
                                                   @RequestParam("firstName") String firstName,
                                                   @RequestParam("lastName") String lastName,
                                                   @RequestParam("username") String username,
                                                   @RequestParam("email") String email,
                                                   @RequestParam("role") String role,
                                                   @RequestParam("isActive") String isActive,
                                                   @RequestParam("isNonLocked") String isNonLocked,
                                                   @RequestParam(value = "profileImage", required = false) MultipartFile profilImage) throws UserNotFoundException, EmailExistException, MessagingException, IOException, UsernameExistException {
        Customer customerUpdated = userService.updateCustomer(currentUsername, firstName, lastName, username, email, role, Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNonLocked), profilImage);
        return new ResponseEntity<Customer>(customerUpdated, OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<Customer> findCustomer(@PathVariable("username") String username){
        Customer customerByUsername = userService.findByUsername(username);
        return new ResponseEntity<Customer>(customerByUsername, OK);

    }

    @GetMapping("/list")
    public List<Customer> findAllCustomers(){
        List<Customer> customers = userService.getCustomers();
        return customers;
    }


    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws UserNotFoundException, MessagingException {
        userService.resetPassword(email);
        return response(OK, EMAIL_SENT+email);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteCustomer(@PathVariable("id") long id) {
        userService.deleteCustomer(id);
        return response(OK, CUSTOMER_DELETED_MESSAGE);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<HttpResponse>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase()), httpStatus);

    }

    @GetMapping(value = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
        Customer customerByUsername = userService.findByUsername(username);
        byte[] profileImage = Files.readAllBytes(Path.of(USER_FOLDER + username + FORWARD_SLASH + fileName));
        return profileImage;
    }

    @GetMapping(value = "/image/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTemporaryProfileImage(@PathVariable("username") String username) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] chunk = new byte[1024];
        boolean ifContinueReading;
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        InputStream is = url.openStream();
        while(ifContinueReading = is.read(chunk) > 0){
            int chunkSize = is.read(chunk);
            byteArrayOutputStream.write(chunk, 0, chunkSize);
        }
        return byteArrayOutputStream.toByteArray();
    }


    @PostMapping("/updateProfileImage")
    public ResponseEntity<Customer> updateProfileImage(@RequestParam("username") String username, @RequestParam("profilImage") MultipartFile profilImage) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        Customer customer = userService.updateProfileImage(username, profilImage);
        return new ResponseEntity<Customer>(customer, OK);
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
