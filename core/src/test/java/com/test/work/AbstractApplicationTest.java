package com.test.work;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@SpringBootTest(classes = {TestWorkApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@AutoConfigureEmbeddedDatabase(
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        replace = AutoConfigureEmbeddedDatabase.Replace.ANY
)
@AutoConfigureWireMock(port = 8081) // Указываем явно порт для WireMock
public abstract class AbstractApplicationTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected WireMockServer wireMockServer;
}
