package com.k1ts.task;

import com.k1ts.helper.UserHelperService;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.subject.SubjectIdDefiner;
import com.k1ts.task.request.GetTaskResponse;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetTaskIntegrationTests {

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
    private SubjectIdDefiner subjectIdDefiner;

    @DisplayName("Test that if course id is invalid then response returns invalidCourseId")
    @Test
    public void testThatIfCourseIdIsInvalidThenResponseReturnsInvalidCourseId() {
        String username = userHelperService.generateStudent();

        int[] invalidCourseIds = {TaskConstants.MIN_COURSE_NUMBER - 1, TaskConstants.MAX_COURSE_NUMBER + 1};

        int year = 2024;
        int subjectId = 1;
        int practiceId = 1;
        int taskId = 1;

        GetTaskResponse expected = GetTaskResponse.failed(GetTaskResponse.Error.invalidCourseId);

        for (int courseId : invalidCourseIds) {
            GetTaskResponse response = sendGetTaskRequest(username, year, courseId, subjectId, practiceId, taskId);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test that if subject id is invalid then response returns invalidSubjectId")
    @Test
    public void testThatIfSubjectIdIsInvalidThenResponseReturnsInvalidSubjectId() {
        String username = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int practiceId = 1;
        int taskId = 1;

        int subjectCount = Objects.requireNonNull(new File("materials/" + year + "/" + courseId + "_course/").listFiles()).length;

        int[] invalidSubjectIds = {TaskConstants.MIN_SUBJECT_ID - 1, subjectCount + 1};

        GetTaskResponse expected = GetTaskResponse.failed(GetTaskResponse.Error.invalidSubjectId);

        for (int subjectId : invalidSubjectIds) {
            GetTaskResponse response = sendGetTaskRequest(username, year, courseId, subjectId, practiceId, taskId);

            Assertions.assertEquals(expected, response);
        }
    }


    @DisplayName("Test that if practice id is invalid then response returns invalidPracticeId")
    @Test
    public void testThatIfPracticeIdIsInvalidThenResponseReturnsInvalidPracticeId() {
        String username = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int taskId = 1;

        int practiceCount = Objects.requireNonNull(new File("materials/" + year + "/" + courseId + "_course/" +
                subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) +
                "/practices/").listFiles()).length;

        int[] invalidPracticeIds = {TaskConstants.MIN_PRACTICE_ID - 1, practiceCount + 1};

        GetTaskResponse expected = GetTaskResponse
                .builder()
                .success(false)
                .error(GetTaskResponse.Error.invalidPracticeId)
                .build();

        for (int practiceId : invalidPracticeIds) {
            GetTaskResponse response = sendGetTaskRequest(username, year, courseId, subjectId, practiceId, taskId);

            Assertions.assertEquals(expected, response);
        }

    }

    @DisplayName("Test that if task id is invalid then response returns invalidTaskId")
    @Test
    public void testThatIfTaskIdIsInvalidThenResponseReturnsInvalidTaskId() {
        String username = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 1;

        int taskCount = Objects.requireNonNull(new File("materials/" + year + "/" + courseId + "_course/" +
                subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) +
                "/practices/" + "practice_" + practiceId).listFiles()).length;

        int[] invalidTaskIds = {TaskConstants.MIN_TASK_ID - 1, taskCount + 1};

        GetTaskResponse expected = GetTaskResponse
                .builder()
                .success(false)
                .error(GetTaskResponse.Error.invalidTaskId)
                .build();

        for (int taskId : invalidTaskIds) {
            GetTaskResponse response = sendGetTaskRequest(username, year, courseId, subjectId, practiceId, taskId);

            Assertions.assertEquals(expected, response);
        }
    }

    @DisplayName("Test that if practice id and task id are valid then text of document is returned")
    @Test
    public void testThatIfPracticeIdAndTaskIdAreValidThenTextOfDocumentIsReturned() {
        String username = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 1;
        int taskId = 1;

        GetTaskResponse response = sendGetTaskRequest(username, year, courseId, subjectId, practiceId, taskId);

        String expectedTaskTest;
        try {
            expectedTaskTest = Files.readString(Path.of("materials/" + year + "/2_course/oop/practices/practice_1/task_1.md"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        expectedTaskTest = expectedTaskTest.replace("\r", "");

        GetTaskResponse expected = GetTaskResponse
                .builder()
                .success(true)
                .error(GetTaskResponse.Error.ok)
                .taskText(expectedTaskTest)
                .build();

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that non-authorized user CAN NOT get task")
    @Test
    public void testThatNonAuthorizedUserCanNotGetTask() {
        String username = "NON-AUTHORIZED-USER";

        int year = 2024;
        int courseId = 2;
        int subjectId = 1;
        int practiceId = 1;
        int taskId = 1;

        GetTaskResponse response = sendGetTaskRequest(username, year, courseId, subjectId, practiceId, taskId);

        GetTaskResponse expected = GetTaskResponse
                .builder()
                .success(false)
                .error(GetTaskResponse.Error.invalidCredentials)
                .build();

        Assertions.assertEquals(expected, response);
    }

    public GetTaskResponse sendGetTaskRequest(String username, int year, int courseId, int subjectId, int practiceId, int taskId) {
        String accessToken = jwtService.generateToken(username);

        if (!userService.exists(username)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String getTaskLink = "http://localhost:" + port + "/getTask?year={1}&courseId={2}&subjectId={3}&practiceId={4}&taskId={5}";
        ResponseEntity<GetTaskResponse> sendCodeResponseEntity =
                restTemplate.exchange(getTaskLink, HttpMethod.GET, entity, GetTaskResponse.class, year, courseId, subjectId, practiceId, taskId);

        GetTaskResponse response = sendCodeResponseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCode = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCode, sendCodeResponseEntity.getStatusCode().value());

        return response;
    }
}
