package com.k1ts.practice;

import com.k1ts.check.CheckService;
import com.k1ts.check.request.check.CheckResponse;
import com.k1ts.helper.UserHelperService;
import com.k1ts.practice.request.practice_toggle.PracticeToggleRequest;
import com.k1ts.practice.request.practice_toggle.PracticeToggleResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PracticeToggleIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CheckService checkService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserHelperService userHelperService;

    // TODO Test that admin can disable practices for student
    @DisplayName("Test that admin can disable practices for student")
    @Test
    public void testThatAdminCanDisablePracticesForStudent() {
        String adminUsername = userHelperService.generateAdmin();

        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        boolean allowed = false;

        PracticeToggleRequest request = PracticeToggleRequest
                .builder()
                .username(studentUsername)
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .allowed(allowed)
                .build();

        PracticeToggleResponse response = sendPracticeToggleRequest(request, adminUsername);

        PracticeToggleResponse expected = PracticeToggleResponse.success(allowed);

        Assertions.assertEquals(expected, response);

        int practiceId = 1;

        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse checkResponse = checkService.check(
                studentUsername, year, courseId, subjectId, practiceId, taskId, code);

        Assertions.assertFalse(checkResponse.isSuccess());
        Assertions.assertEquals(CheckResponse.Error.practiceIsNotAllowed, checkResponse.getError());
    }

    // TODO Test that admin can enable practices for student
    @DisplayName("Test that admin can enable practices for student")
    @Test
    public void testThatAdminCanEnablePracticesForStudent() {
        String adminUsername = userHelperService.generateAdmin();

        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        boolean allowed = true;

        PracticeToggleRequest request = PracticeToggleRequest
                .builder()
                .username(studentUsername)
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .allowed(allowed)
                .build();

        PracticeToggleResponse response = sendPracticeToggleRequest(request, adminUsername);

        PracticeToggleResponse expected = PracticeToggleResponse.success(allowed);

        Assertions.assertEquals(expected, response);

        int practiceId = 1;

        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse checkResponse = checkService.check(
                studentUsername, year, courseId, subjectId, practiceId, taskId, code);

        Assertions.assertTrue(checkResponse.isSuccess());
        Assertions.assertEquals(CheckResponse.Error.ok, checkResponse.getError());
        Assertions.assertTrue(checkResponse.isTaskPassed());
    }

    // TODO Test that admin can disable specific subject for student
    // TODO Test that admin can enable specific subject for student
    // TODO Test that not admin can not disable practices for student
    // TODO Test that non-authorized user can not disable practices for student

    private PracticeToggleResponse sendPracticeToggleRequest(PracticeToggleRequest request, String username) {
        String accessToken = jwtService.generateToken(username);

        if (!userService.exists(username)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<PracticeToggleRequest> entity = new HttpEntity<>(request, headers);

        String togglePracticesLink = "http://localhost:" + port + "/practice/toggle";

        ResponseEntity<PracticeToggleResponse> responseEntity = restTemplate.exchange(
                togglePracticesLink, HttpMethod.POST, entity, PracticeToggleResponse.class);

        PracticeToggleResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int statusCode = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(statusCode, responseEntity.getStatusCode().value());

        return response;
    }
}
