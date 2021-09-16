package com.dev10.userapi.utility;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("api")
public class ApiProperties {
    private String jwtSecretKey;

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }
}
