package com.k1ts.user;

import com.k1ts.helper.DataGenerator;
import com.k1ts.helper.UserHelperService;
import com.k1ts.security.JwtService;
import com.k1ts.security.request.JwtAuthenticationResponse;
import com.k1ts.user.request.SignInRequest;
import com.k1ts.user.request.SignUpRequest;
import com.k1ts.user.request.verify.VerifyAccountRequest;
import com.k1ts.user.request.verify.VerifyAccountResponse;
import com.k1ts.verification.VerificationTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSignInIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserHelperService userHelperService;

    @DisplayName("Test that user with invalid username CAN NOT sign in")
    @Test
    public void testThatUserWithInvalidEmailCanNotSignIn() {
        String username = userHelperService.generateUser();

        SignInRequest request = SignInRequest
                .builder()
                .email("INVALID_EMAIL")
                .password(username)
                .build();

        JwtAuthenticationResponse actual = sendSignInRequest(request);

        JwtAuthenticationResponse expected =
                JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidEmailOrPassword);

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Test that user with invalid password CAN NOT sign in")
    @Test
    public void testThatUserWithInvalidPasswordCanNotSignIn() {
        String username = userHelperService.generateUser();

        SignInRequest request = SignInRequest
                .builder()
                .email(username)
                .password(username + "1234")
                .build();

        JwtAuthenticationResponse actual = sendSignInRequest(request);

        JwtAuthenticationResponse expected =
                JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidEmailOrPassword);

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Test that not verified user can not sign in")
    @Test
    public void testThatNotVerifiedUserCanNotSignIn() {
        String username = userHelperService.generateUser();

        SignInRequest request = SignInRequest
                .builder()
                .email(username)
                .password(username)
                .build();

        JwtAuthenticationResponse response = sendSignInRequest(request);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(JwtAuthenticationResponse.Error.notVerifiedUser, response.getError());
    }

    @DisplayName("Test that user with valid data can sign in")
    @Test
    public void testThatUserWithValidDataCanSignIn() {
        SignUpRequest signUpRequest = createValidRequest();

        sendUserRegisterRequest(signUpRequest);

        SignInRequest request = SignInRequest
                .builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();

        String token = verificationTokenService.getTokensByUsername(signUpRequest.getEmail()).stream().findFirst().orElse(null);

        sendVerifyAccountRequest(token);

        JwtAuthenticationResponse response = sendSignInRequest(request);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(JwtAuthenticationResponse.Error.ok, response.getError());
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
                userRegisterLink, HttpMethod.POST, entity, VerifyAccountResponse.class, token);

        Assertions.assertEquals(200, responseEntity.getStatusCode().value());

        Assertions.assertNotNull(responseEntity);

        return responseEntity.getBody();
    }

    private JwtAuthenticationResponse sendSignInRequest(SignInRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<SignInRequest> entity = new HttpEntity<>(request, headers);

        String signInLink = "http://localhost:" + port + "/auth/signIn";

        ResponseEntity<JwtAuthenticationResponse> responseEntity =
                restTemplate.exchange(signInLink, HttpMethod.POST, entity, JwtAuthenticationResponse.class);

        return responseEntity.getBody();
    }
}
