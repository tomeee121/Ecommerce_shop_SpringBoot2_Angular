package pl.project.wwsis.ecommerceshop.service.MailSenderBeanImpls;

import org.springframework.stereotype.Service;

import static pl.project.wwsis.ecommerceshop.constant.EmailConstant.MANAGER_AUTHORITIES_GRANTED_MSG;

@Service
public class MailSenderBeanForManager extends MailSenderBean {
    @Override
    String getInfoAboutAuthoritiesForEmail() {
        return MANAGER_AUTHORITIES_GRANTED_MSG;
    }
}
