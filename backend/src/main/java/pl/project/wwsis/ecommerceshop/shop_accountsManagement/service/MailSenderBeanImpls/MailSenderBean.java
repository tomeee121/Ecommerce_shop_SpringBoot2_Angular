package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service.MailSenderBeanImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static pl.project.wwsis.ecommerceshop.shop_accountsManagement.constant.EmailConstant.EMAIL_SUBJECT;

@Service
public abstract class MailSenderBean {

    @Autowired
    private JavaMailSender javaMailSender;
    Logger logger = LoggerFactory.getLogger(MailSender.class);

    abstract String getInfoAboutAuthoritiesForEmail();

    public void sendEmail(String firstName, String password, String to) throws MessagingException {
        String infoAboutAuthoritiesForEmail = getInfoAboutAuthoritiesForEmail();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(EMAIL_SUBJECT);
        mimeMessageHelper.setText("Hello "+firstName+" by the Ecommerce shop, <br>"+infoAboutAuthoritiesForEmail+ "<br> We would like to pass you the password for our website: "+password+", <br> With kind regards - our administration", true);
        javaMailSender.send(mimeMessage);
    }

    public void sendNewPasswordEmail(String firstName, String password, String to) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(EMAIL_SUBJECT);
        mimeMessageHelper.setText("Hello "+firstName+" by the Ecommerce shop, <br> Your password has been re-generated to: "+password+", <br> With kind regards - our administration", true);
        javaMailSender.send(mimeMessage);
    }

}
