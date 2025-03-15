package test.java.com.test.work.controller.auth;

import com.test.work.AbstractApplicationTest;
import com.test.work.SpringBootIt;
import com.test.work.model.UserCredits;
import com.test.work.model.UserTokens;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.test.work.util.TokenUtil.tokenRUSINOX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootIt
@AutoConfigureMockMvc
public class AuthControllerTest_JWT extends AbstractApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("login and refresh")
    void case1() throws Exception{
        var request = new UserCredits();
        request.setLogin("useradmin@mail.ru");
        request.setPassword("R6tY3q9Zx1");

        var response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        var responseBody = response.getResponse().getContentAsString();
        var tokens = objectMapper.readValue(responseBody, UserTokens.class);

        Assertions.assertNotNull(tokens.getAccessToken());


        var response2 = mockMvc.perform(post("/refresh")
                        .header("Authorization", "Bearer " + tokenRUSINOX(objectMapper, mockMvc))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokens)))
                .andExpect(status().isOk())
                .andReturn();

        var responseBody2 = response2.getResponse().getContentAsString();
        var tokens2 = objectMapper.readValue(responseBody2, UserTokens.class);

        Assertions.assertNotNull(tokens2.getAccessToken());
    }

}
