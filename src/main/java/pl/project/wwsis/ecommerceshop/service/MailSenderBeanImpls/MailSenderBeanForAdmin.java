package pl.project.wwsis.ecommerceshop.service.MailSenderBeanImpls;

import org.springframework.stereotype.Service;

import static pl.project.wwsis.ecommerceshop.constant.EmailConstant.ADMIN_AUTHORITIES_GRANTED_MSG;

@Service
public class MailSenderBeanForAdmin extends MailSenderBean {
    @Override
    String getInfoAboutAuthoritiesForEmail() {
        return ADMIN_AUTHORITIES_GRANTED_MSG;
    }
}
