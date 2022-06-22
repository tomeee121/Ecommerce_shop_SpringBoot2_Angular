package pl.project.wwsis.ecommerceshop.filter.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import pl.project.wwsis.ecommerceshop.model.Customer;
import pl.project.wwsis.ecommerceshop.service.LoginAttemptService;

@Component
public class AuthenticationSuccessListener {

    private LoginAttemptService loginAttemptService;

    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onSuccessAuthentication(AuthenticationSuccessEvent event){
        Object principalObject = event.getAuthentication().getPrincipal();
        if(principalObject instanceof Customer){
            Customer customer = (Customer) principalObject;
            loginAttemptService.evictUserFromLoginAttemptCache(customer.getUsername());
        }


    }
}
