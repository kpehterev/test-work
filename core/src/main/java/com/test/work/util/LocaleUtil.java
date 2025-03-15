package main.java.com.test.work.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Locale;

public class LocaleUtil {

    public static Locale determineLocale(HttpServletRequest request) {
        var acceptLanguage = request.getHeader("Accept-Language");
        if (acceptLanguage == null) {
            return Locale.US;
        }

        var language = Arrays.stream(acceptLanguage.split(";")).findFirst().get();
        if (language.contains("ru")) {
            return Locale.forLanguageTag("ru-RU");
        } else if (language.contains("zh")) {
            return Locale.CHINA;
        } else {
            return Locale.US;
        }
    }
}
