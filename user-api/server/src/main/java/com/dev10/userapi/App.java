package com.dev10.userapi;

import com.dev10.userapi.utility.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApiProperties.class)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
