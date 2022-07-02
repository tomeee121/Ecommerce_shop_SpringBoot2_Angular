package pl.project.wwsis.ecommerceshop.service.MailSenderBeanImpls;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import static pl.project.wwsis.ecommerceshop.constant.EmailConstant.USER_AUTHORITIES_GRANTED_MSG;

@Service
public class MailSenderBeanForNormalUser extends MailSenderBean {
    @Override
    String getInfoAboutAuthoritiesForEmail() {
        System.out.println("wyslano");
        return USER_AUTHORITIES_GRANTED_MSG;

    }
}
