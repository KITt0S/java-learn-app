package com.k1ts.user;

import com.k1ts.email.EmailTestConfiguration;
import com.k1ts.helper.DataGenerator;
import com.k1ts.security.request.JwtAuthenticationResponse;
import com.k1ts.user.request.SignUpRequest;
import com.k1ts.user.request.verify.VerifyAccountRequest;
import com.k1ts.user.request.verify.VerifyAccountResponse;
import com.k1ts.verification.VerificationToken;
import com.k1ts.verification.VerificationTokenService;
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

@Import(EmailTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserVerifyAccountIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    VerificationTokenService verificationTokenService;

    @Autowired
    private UserService userService;

    @DisplayName("Test that user with invalid token can not verify account")
    @Test
    public void testThatUserWithInvalidTokenCanNotVerifyAccount() {
        String[] invalidTokens = {
                DataGenerator.generateRandomString(1, 50),
                DataGenerator.generateRandomString(1, 50),
                DataGenerator.generateRandomString(1, 50)
        };

        for (String token : invalidTokens) {
            VerifyAccountResponse response = sendVerifyAccountRequest(token);

            Assertions.assertFalse(response.isSuccess());
            Assertions.assertEquals(VerifyAccountResponse.Error.invalidToken, response.getError());
        }
    }

    @DisplayName("Test that user with expired token can not verify account")
    @Test
    public void testThatUserWithExpiredTokenCanNotVerifyAccount() {
        SignUpRequest signUpRequest = createValidRequest();

        sendUserRegisterRequest(signUpRequest);

        String tokenId = verificationTokenService.getTokensByUsername(signUpRequest.getEmail()).stream().findFirst().orElse(null);

        VerificationToken token = verificationTokenService.getById(tokenId);

        token.setExpiredAt(System.currentTimeMillis() - 1);
        verificationTokenService.save(token);

        VerifyAccountResponse response = sendVerifyAccountRequest(tokenId);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(VerifyAccountResponse.Error.invalidToken, response.getError());
    }

    @DisplayName("Test that user with valid token can verify account")
    @Test
    public void testThatUserWithValidTokenCanVerifyAccount() {
        SignUpRequest signUpRequest = createValidRequest();

        sendUserRegisterRequest(signUpRequest);

        String token = verificationTokenService.getTokensByUsername(signUpRequest.getEmail()).stream().findFirst().orElse(null);

        VerifyAccountResponse response = sendVerifyAccountRequest(token);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(VerifyAccountResponse.Error.ok, response.getError());

        User user = userService.getById(signUpRequest.getEmail());

        Assertions.assertTrue(user.isVerified());
    }

    private SignUpRequest createValidRequest() {
        return SignUpRequest
                .builder()
                .email(DataGenerator.generateRandomValidEmail())
                .firstName(DataGenerator.generateRandomString(UserConstants.FIRST_NAME_MIN_SIZE, UserConstants.FIRST_NAME_MAX_SIZE))
                .patronymic(DataGenerator.generateRandomString(UserConstants.PATRONYMIC_MIN_SIZE, UserConstants.PATRONYMIC_MAX_SIZE))
                .lastName(DataGenerator.generateRandomString(UserConstants.LAST_NAME_MIN_SIZE, UserConstants.LAST_NAME_MAX_SIZE))
                .password(DataGenerator.generateRandomString(UserConstants.PASSWORD_MIN_SIZE, UserConstants.PASSWORD_MAX_SIZE))
                .courseNumber(DataGenerator.generateRandomNumber(UserConstants.MIN_COURSE_NUMBER, UserConstants.MAX_COURSE_NUMBER))
                .speciality(DataGenerator.generateRandomNumber(UserConstants.SPECIALITIES))
                .build();
    }

    private JwtAuthenticationResponse sendUserRegisterRequest(SignUpRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<SignUpRequest> entity = new HttpEntity<>(request, headers);

        String userRegisterLink = "http://localhost:" + port + "/auth/signup";

        ResponseEntity<JwtAuthenticationResponse> responseEntity = restTemplate.exchange(
                userRegisterLink, HttpMethod.POST, entity, JwtAuthenticationResponse.class);

        Assertions.assertEquals(200, responseEntity.getStatusCode().value());

        Assertions.assertNotNull(responseEntity);

        return responseEntity.getBody();
    }

    private VerifyAccountResponse sendVerifyAccountRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<VerifyAccountRequest> entity = new HttpEntity<>(VerifyAccountRequest.builder().token(token).build(), headers);

        String userRegisterLink = "http://localhost:" + port + "/auth/verify";

        ResponseEntity<VerifyAccountResponse> responseEntity = restTemplate.exchange(
                userRegisterLink, HttpMethod.POST, entity, VerifyAccountResponse.class);

        Assertions.assertEquals(200, responseEntity.getStatusCode().value());

        Assertions.assertNotNull(responseEntity);

        return responseEntity.getBody();
    }
}
