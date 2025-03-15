package com.test.work.email;

import com.test.work.model.User;
import com.test.work.util.EmailNotificationUtils;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final static String EMAIL_TEMPLE = "email/email-temple.html";

    @Value("${spring.mail.sender}")
    private String sender;
    @Value("${spring.mail.recipient}")
    private String recipient;
    private String emailTempleHtml;
    private final JavaMailSender emailSender;

    @PostConstruct
    private void init() throws IOException {
        this.emailTempleHtml = readHtmlTemplate();
    }

    @Async
    public void sendEmail(@NonNull User user, String userLogin, NotificationStatus status) throws MessagingException {
        var templateData = EmailNotificationUtils.createTemplateData(user, userLogin, status);

        var message = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sender);
        helper.setTo(recipient);
        helper.setSubject(templateData.get(EmailNotificationUtils.TITLE));
        helper.setText(replaceTemplateData(templateData), true);
        emailSender.send(message);
    }

    private String replaceTemplateData(@NonNull final Map<String, String> templateData) {
        var htmlContent = this.emailTempleHtml;
        for (var entry : templateData.entrySet()) {
            htmlContent = htmlContent.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return htmlContent;
    }

    private String readHtmlTemplate() throws IOException {
        try (InputStream inputStream = new ClassPathResource(EMAIL_TEMPLE).getInputStream()) {
            byte[] emailTemple = FileCopyUtils.copyToByteArray(inputStream);
            return new String(emailTemple, StandardCharsets.UTF_8);
        }
    }

}