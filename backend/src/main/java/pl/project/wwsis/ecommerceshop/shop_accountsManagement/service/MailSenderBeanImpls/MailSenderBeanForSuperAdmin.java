package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.MailSenderBeanImpls;

import org.springframework.stereotype.Service;

import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.EmailConstant.SUPERADMIN_AUTHORITIES_GRANTED_MSG;

@Service
public class MailSenderBeanForSuperAdmin extends MailSenderBean {
    @Override
    String getInfoAboutAuthoritiesForEmail() {
        return SUPERADMIN_AUTHORITIES_GRANTED_MSG;
    }
}
