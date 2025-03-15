package main.java.com.test.work.i18n;

import io.micrometer.common.lang.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import static com.test.work.security.config.SecurityContext.getLocale;

/**
 * Объект для получения интернац. сообщений для юзера с учетом его локали
 */
@Service
@RequiredArgsConstructor
public class MsgSourceService {

  private final MessageSource messageSource;

  /**
   * Получение сообщения для локали юзера
   *
   * @param code код сообщения
   * @param args аргументы для сообщения
   * @return сообщение или его код, если найти сообщение не удалось
   */
  private String getMessage(String code, Object... args) {
    return messageSource.getMessage(code, args, getLocale());
  }

  public String getTitle(@NonNull final String key) {
    return getMessage(key + ".title", null);
  }

  public String getErrorMessage(@NonNull final String key, @Nullable final Object... args) {
    return getMessage(key + ".message", args);
  }

}