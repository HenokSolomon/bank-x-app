package com.bankx.core.domain.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service("messagingService")
public class MessagingServiceImpl implements MessagingService {

    private JavaMailSender emailSender;
    private SpringTemplateEngine thymeleafTemplateEngine;

    public MessagingServiceImpl(JavaMailSender emailSender, SpringTemplateEngine thymeleafTemplateEngine) {
        this.emailSender = emailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Override
    public void sendEmail(String from, String to, String subject, String templateName, Map<String, Object> templateModel) throws MessagingException {

        Context thymeleafContext = new Context();

        thymeleafContext.setVariables( templateModel );

        String htmlBody = thymeleafTemplateEngine.process( templateName, thymeleafContext );

        sendEmail( from, to, subject, htmlBody );
    }


    private void sendEmail(String from, String to, String subject, String messageBody) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper( message, true, "UTF-8" );
        helper.setTo( to );
        helper.setFrom( from );
        helper.setSubject( subject );
        helper.setText( messageBody, true );

        emailSender.send( message );
    }


}
