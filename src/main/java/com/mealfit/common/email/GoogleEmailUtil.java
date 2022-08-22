package com.mealfit.common.email;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!test")
@Component
public class GoogleEmailUtil implements EmailUtil {

    private final String HOST_USERNAME;
    private final String HOST_PASSWORD;

    public GoogleEmailUtil(@Value("${email.username}") String host_username,
          @Value("${email.password}") String host_password) {
        HOST_USERNAME = host_username;
        HOST_PASSWORD = host_password;
    }

    @Override
    public void sendEmail(String toEmail, SendingEmailStrategy sendingEmailStrategy) {
        log.info("sending email to {}", toEmail);

        Properties props = settingProperties();

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(HOST_USERNAME, HOST_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(HOST_USERNAME));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            sendingEmailStrategy.fillMessage(message);

            // send the message
            Transport.send(message);

        } catch (MessagingException e) {
            throw new IllegalArgumentException("이메일 발송에 실패했습니다. " + e.getMessage());
        }
    }

    private static Properties settingProperties() {
        String hostSMTP = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", hostSMTP);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }
}
