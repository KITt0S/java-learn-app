package com.k1ts.task;


import com.k1ts.helper.UserHelperService;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.task.request.ListPassedTasksResponse;
import com.k1ts.taskresult.TaskResult;
import com.k1ts.taskresult.TaskResultCompositeId;
import com.k1ts.taskresult.TaskResultService;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListPassedTasksIntegrationTests {

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

    // TODO Проверить, что пользователь админ может получить список выполненных заданий студента
    @DisplayName("Test that admin can list passed student tasks")
    @Test
    public void testThatAdminCanListPassedStudentTasks() {
        String adminUsername = userHelperService.generateAdmin();

        String studentUsername = userHelperService.generateStudent();

        int courseId = 1;
        int subjectId = 1;

        TaskResultCompositeId id = TaskResultCompositeId
                .builder()
                .user(userService.getById(studentUsername))
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(1)
                .taskId(1)
                .build();

        taskResultService.save(TaskResult
                .builder()
                .id(id)
                .passed(true)
                .attemptCount(1)
                .mark(100)
                .build());

        ListPassedTasksResponse response = sendListPassedTasksRequest(adminUsername, studentUsername, courseId, subjectId);

        ListPassedTasksResponse expected = ListPassedTasksResponse.success(Map.of(1, List.of(1)));

        Assertions.assertEquals(expected, response);
    }

    // TODO Проверить, что пользователь админ может получить пустой список выполненных заданий студента
    @DisplayName("Test that admin can list empty passed student tasks")
    @Test
    public void testThatAdminCanListEmptyPassedStudentTasks() {
        String adminUsername = userHelperService.generateAdmin();

        String studentUsername = userHelperService.generateStudent();

        int courseId = 1;
        int subjectId = 1;

        ListPassedTasksResponse response = sendListPassedTasksRequest(adminUsername, studentUsername, courseId, subjectId);

        ListPassedTasksResponse expected = ListPassedTasksResponse.success(Collections.emptyMap());

        Assertions.assertEquals(expected, response);
    }

    // TODO Проверить, что пользователь не админ не может получить список выполненных заданий студента
    @DisplayName("Test that not admin can not list passed student tasks")
    @Test
    public void testThatNotAdminCanNotListPassedStudentTasks() {
        String firstStudentUsername = userHelperService.generateStudent();

        String secondStudentUsername = userHelperService.generateStudent();

        int courseId = 1;
        int subjectId = 1;

        TaskResultCompositeId id = TaskResultCompositeId
                .builder()
                .user(userService.getById(secondStudentUsername))
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(1)
                .taskId(1)
                .build();

        taskResultService.save(TaskResult
                .builder()
                .id(id)
                .passed(true)
                .attemptCount(1)
                .mark(100)
                .build());

        ListPassedTasksResponse response = sendListPassedTasksRequest(firstStudentUsername, secondStudentUsername, courseId, subjectId);

        ListPassedTasksResponse expected = ListPassedTasksResponse.failed(ListPassedTasksResponse.Error.insufficientPrivileges);

        Assertions.assertEquals(expected, response);
    }

    // TODO Проверить, что пользователь админ не может получить список выполненных заданий не студента
    @DisplayName("Test that admin can not list passed not student tasks")
    @Test
    public void testThatAdminCanNotListPassedNotStudentTasks() {
        String firstAdminUsername = userHelperService.generateAdmin();

        String secondAdminUsername = userHelperService.generateAdmin();

        String studentUsername = userHelperService.generateStudent();

        int courseId = 1;

        int subjectId = 1;TaskResultCompositeId id = TaskResultCompositeId
                .builder()
                .user(userService.getById(studentUsername))
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(1)
                .taskId(1)
                .build();

        taskResultService.save(TaskResult
                .builder()
                .id(id)
                .passed(true)
                .attemptCount(1)
                .mark(100)
                .build());

        ListPassedTasksResponse response = sendListPassedTasksRequest(firstAdminUsername, secondAdminUsername, courseId, subjectId);

        ListPassedTasksResponse expected = ListPassedTasksResponse.failed(ListPassedTasksResponse.Error.invalidStudentUsername);

        Assertions.assertEquals(expected, response);
    }

    // TODO Проверить, что неавторизованный пользователь не может получить список выполненных заданий студента
    @DisplayName("Test that non-authorized user can not list passed student tasks")
    @Test
    public void testThatNonAuthorizedUserCanNotListPassedStudentTasks() {
        String adminUsername = "NON-AUTHORIZED-USER";

        String studentUsername = userHelperService.generateStudent();

        int courseId = 1;
        int subjectId = 1;

        TaskResultCompositeId id = TaskResultCompositeId
                .builder()
                .user(userService.getById(studentUsername))
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(1)
                .taskId(1)
                .build();

        taskResultService.save(TaskResult
                .builder()
                .id(id)
                .passed(true)
                .attemptCount(1)
                .mark(100)
                .build());

        ListPassedTasksResponse response = sendListPassedTasksRequest(adminUsername, studentUsername, courseId, subjectId);

        ListPassedTasksResponse expected = ListPassedTasksResponse.failed(ListPassedTasksResponse.Error.invalidCredentials);

        Assertions.assertEquals(expected, response);
    }

    private ListPassedTasksResponse sendListPassedTasksRequest(String adminUsername, String studentUsername, int courseId, int subjectId) {
        String accessToken = jwtService.generateToken(adminUsername);

        if (!userService.exists(adminUsername)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String listPassedTasksLink = "http://localhost:" + port + "/listPassedTasks?studentUsername={1}&courseId={2}&subjectId={3}";

        ResponseEntity<ListPassedTasksResponse> responseEntity = restTemplate.exchange(
                listPassedTasksLink,
                HttpMethod.GET,
                entity,
                ListPassedTasksResponse.class,
                studentUsername, courseId, subjectId);

        ListPassedTasksResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCodeValue = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCodeValue, responseEntity.getStatusCode().value());

        return response;
    }
}
