package pl.project.wwsis.ecommerceshop.service.MailSenderBeanImpls;

import org.springframework.stereotype.Service;

import static pl.project.wwsis.ecommerceshop.constant.EmailConstant.HR_AUTHORITIES_GRANTED_MSG;

@Service
public class MailSenderBeanForHR extends MailSenderBean {
    @Override
    String getInfoAboutAuthoritiesForEmail() {
        return HR_AUTHORITIES_GRANTED_MSG;
    }
}
