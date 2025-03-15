package main.java.com.test.work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableConfigurationProperties
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class TestWorkApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestWorkApplication.class, args);
    }

}
