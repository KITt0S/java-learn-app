package com.k1ts.user;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GoogleCredentialsManager {

    @Value("${google.clientid:}")
    private String clientId;

    @Value("${google.clientsecret:}")
    private String clientSecret;

    @Value("${google.redirect_uri:}")
    private String redirectUri;
}
