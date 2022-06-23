package pl.project.wwsis.ecommerceshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static pl.project.wwsis.ecommerceshop.constant.EmailConstant.EMAIL_SUBJECT;

@Service
public class MailSenderBean {

    @Autowired
    private JavaMailSender javaMailSender;
    Logger logger = LoggerFactory.getLogger(MailSender.class);


    public void sendEmail(String firstName, String password, String to) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(EMAIL_SUBJECT);
        mimeMessageHelper.setText("Hello "+firstName+" by the Ecommerce shop, <br> We would like to pass you the password for our website: "+password+", <br> With kind regards - our administration", true);
        javaMailSender.send(mimeMessage);
    }

}
