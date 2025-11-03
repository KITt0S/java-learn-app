package com.k1ts.taskresult;

import com.k1ts.check.request.check.CheckRequest;
import com.k1ts.check.request.check.CheckResponse;
import com.k1ts.helper.UserHelperService;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.taskresult.request.GetStudentTaskResultsResponse;
import com.k1ts.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetStudentPracticeIdTaskIdMarksIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserHelperService userHelperService;

    @Autowired
    private TaskResultService taskResultService;

    @BeforeEach
    public void beforeEach() {
        taskResultService.deleteAll();
        userService.deleteAll();
        Assertions.assertTrue(taskResultService.isEmpty());
        Assertions.assertTrue(userService.isEmpty());
    }

    @DisplayName("Test that admin can get student task results")
    @Test
    public void testThatAdminCanGetStudentTaskResults() {
        String adminUsername = userHelperService.generateAdmin();
        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;

        int practiceId = 1;
        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse checkResponse = sendCheckTaskRequest(studentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(checkResponse.isSuccess());

        GetStudentTaskResultsResponse response = sendGetStudentTaskResultsRequest(
                adminUsername, year, courseId, subjectId, studentUsername);

        GetStudentTaskResultsResponse expected = GetStudentTaskResultsResponse
                .success(Map.of(1, Map.of(
                                        1, 100,
                                        2, 0,
                                        3, 0,
                                        4, 0,
                                        5, 0),
                                2, Map.of(
                                        1, 0,
                                        2, 0,
                                        3, 0,
                                        4, 0,
                                        5, 0)),
                        100/10);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that student can get student task results")
    @Test
    public void testThatStudentCanGetStudentTaskResults() {
        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;

        int practiceId = 1;
        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse checkResponse = sendCheckTaskRequest(studentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(checkResponse.isSuccess());

        GetStudentTaskResultsResponse response = sendGetStudentTaskResultsRequest(
                studentUsername, year, courseId, subjectId, studentUsername);

        GetStudentTaskResultsResponse expected = GetStudentTaskResultsResponse
                .success(Map.of(1, Map.of(
                                        1, 100,
                                        2, 0,
                                        3, 0,
                                        4, 0,
                                        5, 0),
                                2, Map.of(
                                        1, 0,
                                        2, 0,
                                        3, 0,
                                        4, 0,
                                        5, 0)),
                        100/10);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that student can not get student task results of another student")
    @Test
    public void testThatStudentCanNotGetStudentTaskResultOfAnotherStudent() {
        String studentUsername = userHelperService.generateStudent();
        String anotherStudentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;

        int practiceId = 1;
        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse checkResponse = sendCheckTaskRequest(anotherStudentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(checkResponse.isSuccess());

        GetStudentTaskResultsResponse response = sendGetStudentTaskResultsRequest(
                studentUsername, year, courseId, subjectId, anotherStudentUsername);

        GetStudentTaskResultsResponse expected =
                GetStudentTaskResultsResponse.failed(GetStudentTaskResultsResponse.Error.insufficientPrivileges);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that non-authorized user can not get student task results")
    @Test
    public void testThatNonAuthorizedUserCanNotGetStudentTaskResults() {
        String nonAuthorizedUsername = "NON-AUTHORIZED-USER";
        String anotherStudentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;

        int practiceId = 1;
        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse checkResponse = sendCheckTaskRequest(anotherStudentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(checkResponse.isSuccess());

        GetStudentTaskResultsResponse response = sendGetStudentTaskResultsRequest(
                nonAuthorizedUsername, year, courseId, subjectId, anotherStudentUsername);

        GetStudentTaskResultsResponse expected =
                GetStudentTaskResultsResponse.failed(GetStudentTaskResultsResponse.Error.invalidCredentials);

        Assertions.assertEquals(expected, response);
    }

    private CheckResponse sendCheckTaskRequest(String studentUsername, CheckRequest request) {
        String accessToken = jwtService.generateToken(studentUsername);

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<CheckRequest> entity = new HttpEntity<>(request, headers);

        String sendCheckTaskRequestLink = "http://localhost:" + port + "/check";

        ResponseEntity<CheckResponse> responseEntity = restTemplate.exchange(
                sendCheckTaskRequestLink, HttpMethod.POST, entity, CheckResponse.class);

        CheckResponse response = responseEntity.getBody();

        Assertions.assertEquals(200, responseEntity.getStatusCode().value());

        return response;
    }

    private GetStudentTaskResultsResponse sendGetStudentTaskResultsRequest(
            String adminUsername, int year, int courseId, int subjectId, String studentUsername) {
        String accessToken = jwtService.generateToken(adminUsername);

        if (!userService.exists(adminUsername)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String sendCheckTaskRequestLink = "http://localhost:" + port +
                "/getStudentTaskResults?year={1}&courseId={2}&subjectId={3}&studentUsername={4}";

        ResponseEntity<GetStudentTaskResultsResponse> responseEntity = restTemplate.exchange(
                sendCheckTaskRequestLink, HttpMethod.GET, entity, GetStudentTaskResultsResponse.class, year, courseId, subjectId, studentUsername);

        GetStudentTaskResultsResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCodeValue = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCodeValue, responseEntity.getStatusCode().value());

        return response;
    }
}
