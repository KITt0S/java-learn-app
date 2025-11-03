package com.k1ts.task;

import com.k1ts.helper.UserHelperService;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.task.request.ListTasksResponse;
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
public class ListTasksIntegrationTests {

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

    @DisplayName("Test that user can list tasks")
    @Test
    public void testThatUserCanListTasks() {
        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 1;

        ListTasksResponse response = sendListTasksRequest(studentUsername, year, courseId, subjectId, practiceId);

        ListTasksResponse expected = ListTasksResponse.success(5);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that user can not list empty taks")
    @Test
    public void testThatUserCanNotListEmptyTasks() {
        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 2;

        ListTasksResponse response = sendListTasksRequest(studentUsername, year, courseId, subjectId, practiceId);

        ListTasksResponse expected = ListTasksResponse.failed(ListTasksResponse.Error.noTasks);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that non authorized user can not list tasks")
    @Test
    public void testThatNonAuthorizedUserCanNotListTasks() {
        String studentUsername = "NON-AUTHORIZED-USER";

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 1;

        ListTasksResponse response = sendListTasksRequest(studentUsername, year, courseId, subjectId, practiceId);

        ListTasksResponse expected = ListTasksResponse.failed(ListTasksResponse.Error.invalidCredentials);

        Assertions.assertEquals(expected, response);
    }

    private ListTasksResponse sendListTasksRequest(String username, int year, int courseId, int subjectId, int practiceId) {
        String accessToken = jwtService.generateToken(username);

        if (!userService.exists(username)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String listTasksLink = "http://localhost:" + port + "/listTasks?year={1}&courseId={2}&subjectId={3}&practiceId={4}";

        ResponseEntity<ListTasksResponse> responseEntity = restTemplate.exchange(
                listTasksLink, HttpMethod.GET, entity, ListTasksResponse.class, year, courseId, subjectId, practiceId);

        ListTasksResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCodeValue = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCodeValue, responseEntity.getStatusCode().value());

        return response;
    }
}
