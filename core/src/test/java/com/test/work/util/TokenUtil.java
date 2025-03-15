package test.java.com.test.work.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UtilityClass
public class TokenUtil {

    public static String tokenRUSINOX(ObjectMapper mapper, MockMvc mockMvc) throws Exception {
        return token("userrusinox@mail.ru", "X4vB5jM1sQ", mapper, mockMvc);
    }
    private static String token(String username, String password, ObjectMapper mapper, MockMvc mockMvc) throws Exception {
        // Подготовка запроса на аутентификацию
        var body = "{\"login\":\"" + username + "\",\"password\":\"" + password + "\"}";

        // Выполнение запроса на аутентификацию
        var result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        // Извлечение токена из ответа
        var response = result.getResponse().getContentAsString();
        var tokenResponse = mapper.readValue(response, TokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    @Getter
    @Setter
    private static class TokenResponse {
        private String accessToken;
        private String refreshToken;

    }
}
