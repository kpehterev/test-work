package main.java.com.test.work.util;

import com.test.work.email.NotificationStatus;
import com.test.work.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class EmailNotificationUtils {

    public final String CREATE_USER_NOTIFICATION = "Создан новый пользователь";
    public final String GREETING_CREATE_USER_NOTIFICATION = "Уважаемый Администратор, в системе был создан новый пользователь.";
    public final String CHANGE_USER_NOTIFICATION = "Изменение данных пользователя";
    public final String GREETING_CHANGE_USER_NOTIFICATION = "Уважаемый Администратор, в системе были изменены данные пользователя.";

    public final String DETAIL_DATE_CREATE = "Дата и время создания";
    public final String DETAIL_DATE_CHANGE = "Дата и время изменения";
    public final String USER_CREATE = "Создано пользователем";
    public final String USER_CHANGE = "Изменено пользователем";

    public static final String TITLE = "title";
    private final String GREETING = "greeting";
    private final String DETAIL_DATE_KEY = "detail_date_key";
    private final String DETAIL_DATE = "detail_date";
    private final String USER_LOGIN_KEY = "user_login_key";
    private final String USER_LOGIN = "user_login";
    private final String USER_NAME = "user_name";
    private final String USER_EMAIL = "user_email";

    /**
     * Создает данные для шаблона уведомления.
     *
     * @param user      Пользователь, о котором идет уведомление.
     * @param userLogin Логин пользователя, который выполнил действие.
     * @param status    Статус уведомления (CREATE_USER или CHANGE_USER).
     * @return Map с данными для шаблона.
     */
    public static Map<String, String> createTemplateData(
            User user, String userLogin, NotificationStatus status
    ) {
        String subject;
        String greeting;
        String detailDateKey;
        String userLoginKey;
        final String detailDate = LocalDateTime.now().format(DateTimeFormatterUtil.FORMATTER_YYYY_MM_DD_HH_MM_SS);

        switch (status) {
            case CREATE -> {
                subject = CREATE_USER_NOTIFICATION;
                greeting = GREETING_CREATE_USER_NOTIFICATION;
                detailDateKey = DETAIL_DATE_CREATE;
                userLoginKey = USER_CREATE;
            }
            case CHANGE_CONTENT -> {
                subject = CHANGE_USER_NOTIFICATION;
                greeting = GREETING_CHANGE_USER_NOTIFICATION;
                detailDateKey = DETAIL_DATE_CHANGE;
                userLoginKey = USER_CHANGE;
            }
            default -> throw new IllegalArgumentException("Неизвестный статус уведомления: " + status);
        }

        Map<String, String> templateData = new HashMap<>();
        templateData.put(TITLE, subject);
        templateData.put(GREETING, greeting);
        templateData.put(DETAIL_DATE_KEY, detailDateKey);
        templateData.put(DETAIL_DATE, detailDate);
        templateData.put(USER_LOGIN_KEY, userLoginKey);
        templateData.put(USER_LOGIN, userLogin);
        templateData.put(USER_NAME, user.getName());
        return templateData;
    }
}