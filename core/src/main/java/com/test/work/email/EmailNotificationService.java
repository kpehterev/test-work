package main.java.com.test.work.email;

import com.test.work.exception.ErrorResponseException;
import com.test.work.model.User;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.test.work.security.config.SecurityContext.getUserRole;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {


    private final EmailSenderService senderService;

    public void sendNotification(@NonNull User user, NotificationStatus status) throws ErrorResponseException {

        try {
            var userLogin = getUserRole();
            senderService.sendEmail(user, userLogin.name(), status);
            log.info("Email saved successfully by user: {}", userLogin);
        }catch (MessagingException e){
            log.error("Не удалось отправить email по причине : " + e);
        }catch (Exception e){
            log.error("Ошибка при отправке уведомления : " + e);
        }
    }

}
