package pl.project.wwsis.ecommerceshop.shop_accountsManagement.filter.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.LoginAttemptService;

@Component
public class AuthenticationFailedListener {

    private LoginAttemptService loginAttemptService;

    public AuthenticationFailedListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationFail(AuthenticationFailureBadCredentialsEvent event){
        Object principalObject = event.getAuthentication().getPrincipal();
        if(principalObject instanceof String){
            String principal = (String) principalObject;
            loginAttemptService.addUserToLoginAttemptCache(principal);
        }

    }
}
