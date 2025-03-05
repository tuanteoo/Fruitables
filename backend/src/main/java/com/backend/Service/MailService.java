package com.backend.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String emailFrom;

    public void sendVerificationEmail(String recipients, String subject, String templateName, Context context) throws UnsupportedEncodingException {
        try{
            String htmlContent = templateEngine.process(templateName, context);

            // Create MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(emailFrom, "Fruitables");
            helper.setTo(recipients);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(mimeMessage);
        }
        catch (MessagingException e){
            throw new RuntimeException("Failed to send email: ", e);
        }
    }

}
