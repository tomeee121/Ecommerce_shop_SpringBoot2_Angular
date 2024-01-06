package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.MailSenderBeanImpls;

import org.springframework.stereotype.Service;

import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.EmailConstant.ADMIN_AUTHORITIES_GRANTED_MSG;

@Service
public class MailSenderBeanForAdmin extends MailSenderBean {
    @Override
    String getInfoAboutAuthoritiesForEmail() {
        return ADMIN_AUTHORITIES_GRANTED_MSG;
    }
}
