package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.MailSenderBeanImpls;

import org.springframework.stereotype.Service;

import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.EmailConstant.USER_AUTHORITIES_GRANTED_MSG;

@Service
public class MailSenderBeanForNormalUser extends MailSenderBean {
    @Override
    String getInfoAboutAuthoritiesForEmail() {
        System.out.println("wyslano");
        return USER_AUTHORITIES_GRANTED_MSG;

    }
}
