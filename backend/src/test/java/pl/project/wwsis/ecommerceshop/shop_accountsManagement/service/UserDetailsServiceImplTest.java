package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.config.S3Buckets;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.MailSenderBeanImpls.*;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO.CustomerRepo;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DAO.OrderRepo;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.model.Customer;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    private S3Client s3Client;
    private CustomerRepo customerRepo;
    private Authentication authentication;
    private SecurityContext securityContext;
    private S3Buckets s3Buckets;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private LoginAttemptService loginAttemptService;
    private MailSenderBeanForNormalUser mailSenderBeanForNormalUser;
    private MailSenderBeanForHR mailSenderBeanForHR;
    private MailSenderBeanForManager mailSenderBeanForManager;
    private MailSenderBeanForSuperAdmin mailSenderBeanForSuperAdmin;
    private MailSenderBeanForAdmin mailSenderBeanForAdmin;
    private OrderRepo orderRepo;
    private PasswordGenerator passwordGenerator;
    private S3Service s3Service;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        s3Client = mock(S3Client.class);
        customerRepo = mock(CustomerRepo.class);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        s3Buckets = mock(S3Buckets.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        loginAttemptService = mock(LoginAttemptService.class);
        mailSenderBeanForNormalUser = mock(MailSenderBeanForNormalUser.class);
        mailSenderBeanForHR = mock(MailSenderBeanForHR.class);
        mailSenderBeanForManager = mock(MailSenderBeanForManager.class);
        mailSenderBeanForSuperAdmin = mock(MailSenderBeanForSuperAdmin.class);
        mailSenderBeanForAdmin = mock(MailSenderBeanForAdmin.class);
        orderRepo = mock(OrderRepo.class);
        passwordGenerator = mock(PasswordGenerator.class);
        s3Service = mock(S3Service.class);
        userDetailsService = new UserDetailsServiceImpl(customerRepo, bCryptPasswordEncoder, loginAttemptService, mailSenderBeanForNormalUser,
                mailSenderBeanForHR, mailSenderBeanForManager, mailSenderBeanForSuperAdmin,
                mailSenderBeanForAdmin, orderRepo, passwordGenerator, s3Service, s3Buckets);
    }

    @Test
    void canUploadProfileImage() throws IOException {
        //given
        String username = "tomeee121";
        String bucketName = "ecommerce-shop-bucket";
        Customer customer = new Customer("Tomasz", "Borowski", "tomeklfc@o2.pl");
        when(customerRepo.findCustomerByUsername(username)).thenReturn(Optional.of(customer));

        when(authentication.getPrincipal()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(customerRepo.getProfileImageKeyByUsername(username)).thenReturn(null);
        when(s3Buckets.getEcommerce()).thenReturn(bucketName);
        MockMultipartFile multipartFile = new MockMultipartFile("file", "Hello world!".getBytes());
        byte[] multipartFileBytes = multipartFile.getBytes();

        ArgumentCaptor<String> generatedKeyCaptor = ArgumentCaptor.forClass(String.class);

        //when
        userDetailsService.uploadImageToS3(username, multipartFile);

        //then
        verify(s3Service).putObject(eq(bucketName), generatedKeyCaptor.capture(), eq(multipartFileBytes));
        verify(customerRepo).updateProfileImageKey(generatedKeyCaptor.getValue());
    }

    @Test
    void canNotUploadProfileImageWhenCustomerNotExisting() throws IOException {
        //given
        String username = "tomeee121";
        when(customerRepo.findCustomerByUsername(username)).thenReturn(Optional.ofNullable(null));

        when(authentication.getPrincipal()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MockMultipartFile multipartFile = new MockMultipartFile("file", "Hello world!".getBytes());
        byte[] multipartFileBytes = multipartFile.getBytes();

        //when
        assertThatThrownBy(() -> userDetailsService.uploadImageToS3(username, multipartFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Such username does not exist!");

        //then
        verifyNoInteractions(s3Service);
        verifyNoInteractions(s3Buckets);
        verify(customerRepo).findCustomerByUsername(username);
        verify(customerRepo, never()).updateProfileImageKey(anyString());
    }

    @Test
    void canNotUploadProfileImageWhenTokenIsNotRight() throws IOException {
        //given
        String username = "tomeee121";

        MockMultipartFile multipartFile = new MockMultipartFile("file", "Hello world!".getBytes());
        byte[] multipartFileBytes = multipartFile.getBytes();

        //when
        assertThatThrownBy(() -> userDetailsService.uploadImageToS3(username, multipartFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cannot invoke \"org.springframework.security.core.Authentication.getPrincipal()\" because the return value of \"org.springframework.security.core.context.SecurityContext.getAuthentication()\" is null");

        //then
        verifyNoInteractions(s3Service);
        verifyNoInteractions(s3Buckets);
        verify(customerRepo, never()).updateProfileImageKey(anyString());
    }

    @Test
    void canNotUploadProfileImageWhenMultiPartFileIsBroken() throws IOException {
        //given
        String username = "tomeee121";
        Customer customer = new Customer("Tomasz", "Borowski", "tomeklfc@o2.pl");
        when(customerRepo.findCustomerByUsername(username)).thenReturn(Optional.of(customer));

        when(authentication.getPrincipal()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MockMultipartFile multipartFile = mock(MockMultipartFile.class);
        when(multipartFile.getBytes()).thenThrow(IOException.class);

        //when
        assertThatThrownBy(() -> userDetailsService.uploadImageToS3(username, multipartFile))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(IOException.class);

        //then
        verify(customerRepo).findCustomerByUsername(username);
        verify(customerRepo, never()).updateProfileImageKey(anyString());
    }
}