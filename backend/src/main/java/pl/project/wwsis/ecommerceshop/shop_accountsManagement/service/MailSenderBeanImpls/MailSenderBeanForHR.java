package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.MailSenderBeanImpls;

import org.springframework.stereotype.Service;

import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.EmailConstant.HR_AUTHORITIES_GRANTED_MSG;

@Service
public class MailSenderBeanForHR extends MailSenderBean {
    @Override
    String getInfoAboutAuthoritiesForEmail() {
        return HR_AUTHORITIES_GRANTED_MSG;
    }
}
