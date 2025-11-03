package com.k1ts.password;

import com.k1ts.email.Email;
import com.k1ts.email.EmailTestConfiguration;
import com.k1ts.helper.UserHelperService;
import com.k1ts.password.request.restore.PasswordRestoreRequest;
import com.k1ts.password.request.restore.PasswordRestoreResponse;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@Import(EmailTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PasswordRestoreIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmailTestConfiguration emailTestConfiguration;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserHelperService userHelperService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRestoreTokenService userRestoreTokenService;

    @DisplayName("Test that admin can send request to restore password")
    @Test
    public void testThatAdminCanSendRequestToRestorePassword() {
        String adminUsername = userHelperService.generateAdmin();

        PasswordRestoreResponse response = sendPasswordRestoreRequest(adminUsername);

        PasswordRestoreResponse expected = PasswordRestoreResponse.success();

        Assertions.assertEquals(expected, response);

        Set<String> tokens = userRestoreTokenService.getTokensByUsername(adminUsername);

        Email expectedEmail = Email
                .builder()
                .from("noreply@exampleapp.online")
                .to(adminUsername)
                .subject("Відновлення паролю - Java Learn App")
                .content("Посилання на відновлення паролю\n"
                        + "\n"
                        + "js-java-learn-app.vercel.com?token=" + tokens.stream().findFirst().orElse(null))
                .build();

        Email actualEmail = emailTestConfiguration.getEmails().stream().findFirst().orElse(null);

        Assertions.assertEquals(expectedEmail, actualEmail);
    }

    @DisplayName("Test that non-authorized user can not send request to restore password")
    @Test
    public void testThatNonAuthorizedUSerCanNotSendRequestToRestorePassword() {
        String nonAuthorizedUsername = "NON-AUTHORIZED-USER";

        PasswordRestoreResponse response = sendPasswordRestoreRequest(nonAuthorizedUsername);

        PasswordRestoreResponse expected = PasswordRestoreResponse.failed(PasswordRestoreResponse.Error.usernameNotExists);

        Assertions.assertEquals(expected, response);

        Assertions.assertTrue(userRestoreTokenService.getTokensByUsername(nonAuthorizedUsername).isEmpty());

        Assertions.assertTrue(emailTestConfiguration.getEmails().isEmpty());
    }


    private PasswordRestoreResponse sendPasswordRestoreRequest(String username) {
        String accessToken = jwtService.generateToken(username);

        if (!userService.exists(username)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<PasswordRestoreRequest> entity = new HttpEntity<>(new PasswordRestoreRequest(username), headers);

        String passwordRestoreLink = "http://localhost:" + port + "/password/restore";

        ResponseEntity<PasswordRestoreResponse> responseEntity = restTemplate.exchange(
                passwordRestoreLink, HttpMethod.POST, entity, PasswordRestoreResponse.class);

        PasswordRestoreResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCodeValue = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCodeValue, responseEntity.getStatusCode().value());

        return response;
    }
}
