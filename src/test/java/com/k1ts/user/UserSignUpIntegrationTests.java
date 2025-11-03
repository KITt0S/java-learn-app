package com.k1ts.user;

import com.k1ts.email.Email;
import com.k1ts.email.EmailTestConfiguration;
import com.k1ts.helper.DataGenerator;
import com.k1ts.security.request.JwtAuthenticationResponse;
import com.k1ts.user.request.SignUpRequest;
import com.k1ts.user.studentdata.StudentData;
import com.k1ts.user.studentdata.StudentDataService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Import(EmailTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSignUpIntegrationTests {

    @SuppressWarnings("unused")
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmailTestConfiguration emailTestConfiguration;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentDataService studentDataService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @DisplayName("Test that user with invalid email can not sign up")
    @Test
    public void testThatUserWithInvalidEmailCanNotSignUp() {
        String[] invalidEmails = {
                null,
                "",
                "1".repeat(20)
        };

        JwtAuthenticationResponse expected = JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidEmail);

        for (String invalidEmail : invalidEmails) {
            SignUpRequest request = createValidRequest();
            request.setEmail(invalidEmail);

            JwtAuthenticationResponse response = sendUserRegisterRequest(request);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test that user with invalid first name can not register")
    @Test
    public void testThatUserWithInvalidFirstNameCanNotRegister() {
        String[] invalidFirstNames = {
                null,
                "",
                DataGenerator.generateRandomString(UserConstants.FIRST_NAME_MIN_SIZE - 1),
                DataGenerator.generateRandomString(UserConstants.FIRST_NAME_MAX_SIZE + 1)
        };

        JwtAuthenticationResponse expected = JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidFirstName);

        for (String invalidFirstName : invalidFirstNames) {
            SignUpRequest request = createValidRequest();
            request.setFirstName(invalidFirstName);

            JwtAuthenticationResponse response = sendUserRegisterRequest(request);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test that user with invalid patronymic can not register")
    @Test
    public void testThatUserWithInvalidPatronymicCanNotRegister() {
        String[] invalidPatronymics = {
                null,
                "",
                DataGenerator.generateRandomString(UserConstants.PATRONYMIC_MIN_SIZE - 1),
                DataGenerator.generateRandomString(UserConstants.PATRONYMIC_MAX_SIZE + 1)
        };

        JwtAuthenticationResponse expected = JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidPatronymic);

        for (String invalidPatronymic : invalidPatronymics) {
            SignUpRequest request = createValidRequest();
            request.setPatronymic(invalidPatronymic);

            JwtAuthenticationResponse response = sendUserRegisterRequest(request);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test that user with invalid last name can not register")
    @Test
    public void testThatUserWithInvalidLastNameCanNotRegister() {
        String[] invalidLastNames = {
                null,
                "",
                DataGenerator.generateRandomString(UserConstants.LAST_NAME_MIN_SIZE - 1),
                DataGenerator.generateRandomString(UserConstants.LAST_NAME_MAX_SIZE + 1)
        };

        JwtAuthenticationResponse expected = JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidLastName);

        for (String invalidLastName : invalidLastNames) {
            SignUpRequest request = createValidRequest();
            request.setLastName(invalidLastName);

            JwtAuthenticationResponse response = sendUserRegisterRequest(request);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test user with invalid password can not register")
    @Test
    public void testThatUserWithInvalidPasswordCanNotRegister() {
        String[] invalidPasswords = {
                null,
                "",
                DataGenerator.generateRandomString(UserConstants.PASSWORD_MIN_SIZE - 1),
                DataGenerator.generateRandomString(UserConstants.PASSWORD_MAX_SIZE + 1)
        };

        JwtAuthenticationResponse expected = JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidPassword);

        for (String invalidPassword : invalidPasswords) {
            SignUpRequest request = createValidRequest();
            request.setPassword(invalidPassword);

            JwtAuthenticationResponse response = sendUserRegisterRequest(request);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test that user with invalid course number can not register")
    @Test
    public void testThatUserWithInvalidCourseNumberCanNotRegister() {
        int[] invalidCourseNumbers = {UserConstants.MIN_COURSE_NUMBER - 1, UserConstants.MAX_COURSE_NUMBER + 1};

        JwtAuthenticationResponse expected = JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidCourseNumber);

        for (int invalidCourseNumber : invalidCourseNumbers) {
            SignUpRequest request = createValidRequest();
            request.setCourseNumber(invalidCourseNumber);

            JwtAuthenticationResponse response = sendUserRegisterRequest(request);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test that user with invalid speciality can not register")
    @Test
    public void testThatUserWithInvalidSpecialityCanNotRegister() {
        int[] invalidSpecialities = {
                UserConstants.SPECIALITIES[0] - 1,
                UserConstants.SPECIALITIES[UserConstants.SPECIALITIES.length - 1] + 1};

        JwtAuthenticationResponse expected = JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidSpeciality);

        for (int invalidSpeciality : invalidSpecialities) {
            SignUpRequest request = createValidRequest();
            request.setSpeciality(invalidSpeciality);

            JwtAuthenticationResponse response = sendUserRegisterRequest(request);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test that user with existing email can not register")
    @Test
    public void testThatUserWithExistingEmailCanNotRegister() {
        SignUpRequest request = createValidRequest();

        sendUserRegisterRequest(request);

        SignUpRequest secondRequest = createValidRequest();
        secondRequest.setEmail(request.getEmail());

        JwtAuthenticationResponse response = sendUserRegisterRequest(secondRequest);

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(JwtAuthenticationResponse.Error.userAlreadyExists, response.getError());
    }

    @DisplayName("Test that user with valid data can register")
    @Test
    public void testThatUserWithValidDataCanRegister() {
        SignUpRequest request = createValidRequest();

        JwtAuthenticationResponse response = sendUserRegisterRequest(request);

        Assertions.assertTrue(response.isSuccess(), response.getError().name());
        Assertions.assertEquals(JwtAuthenticationResponse.Error.ok, response.getError());

        User user = userService.getById(request.getEmail());

        Assertions.assertEquals(request.getFirstName(), user.getFirstName());
        Assertions.assertEquals(request.getPatronymic(), user.getPatronymic());
        Assertions.assertEquals(request.getLastName(), user.getLastName());
        Assertions.assertEquals(request.getEmail(), user.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(request.getPassword(), user.getPassword()));
        Assertions.assertFalse(user.isVerified());

        StudentData studentData = studentDataService.getByUsername(request.getEmail());

        Assertions.assertEquals(request.getSpeciality(), studentData.getSpeciality());

        Set<String> tokens = verificationTokenService.getTokensByUsername(request.getEmail());

        Email expectedEmail = Email
                .builder()
                .from("noreply@exampleapp.online")
                .to(request.getEmail())
                .subject("Підтвердження email - Java Learn App")
                .content("Посилання на підтвердження email\n"
                        + "\n"
                        + "http://localhost:63342/email_verification.html?token=" + tokens.stream().findFirst().orElse(null))
                .build();

        Email actualEmail = emailTestConfiguration.getEmails().stream().findFirst().orElse(null);

        Assertions.assertEquals(expectedEmail, actualEmail);
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
}
