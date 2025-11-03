package com.k1ts.taskresult;

import com.k1ts.check.request.check.CheckRequest;
import com.k1ts.check.request.check.CheckResponse;
import com.k1ts.helper.UserHelperService;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.taskresult.request.GetStudentTaskResultsBySpecialityResponse;
import com.k1ts.user.User;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetStudentPracticeIdTaskIdMarksBySpecialityIntegrationTests {

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

    @DisplayName("Test that admin can get student task results by speciality")
    @Test
    public void testThatAdminCanGetStudentTaskResultsBySpeciality() {
        int speciality = 121;

        String adminUsername = userHelperService.generateAdmin();
        String studentUsername = userHelperService.generateStudent(UserHelperService.StudentParams
                .builder()
                .speciality(speciality)
                .build());

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

        CheckResponse firstCheckResponse = sendCheckTaskRequest(studentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(firstCheckResponse.isSuccess());
        Assertions.assertEquals(100, firstCheckResponse.getMark());

        GetStudentTaskResultsBySpecialityResponse response = sendGetStudentTaskResultsBySpecialityRequest(
                adminUsername,
                year,
                courseId,
                subjectId,
                speciality);

        User firstStudent = userService.getById(studentUsername);

        List<GetStudentTaskResultsBySpecialityResponse.StudentData> studentDataList = new ArrayList<>(List.of(
                new GetStudentTaskResultsBySpecialityResponse.StudentData(
                        firstStudent.getLastName() + " " + firstStudent.getFirstName(),
                        List.of(
                                new GetStudentTaskResultsBySpecialityResponse.StudentData.PracticeIdTaskIdMarks(
                                        1,
                                        List.of(
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(1, 100),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(2, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(3, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(4, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(5, 0))),
                                new GetStudentTaskResultsBySpecialityResponse.StudentData.PracticeIdTaskIdMarks(
                                        2,
                                        List.of(
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(1, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(2, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(3, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(4, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(5, 0)))
                        ),
                        10)));

        studentDataList.sort(Comparator.comparing(GetStudentTaskResultsBySpecialityResponse.StudentData::getStudentDetails));

        GetStudentTaskResultsBySpecialityResponse expected = GetStudentTaskResultsBySpecialityResponse.success(studentDataList);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that admin can get two student task results by speciality")
    @Test
    public void testThatAdminCanGetTwoStudentTaskResultsBySpeciality() {
        int speciality = 121;

        String adminUsername = userHelperService.generateAdmin();
        String studentUsername = userHelperService.generateStudent(UserHelperService.StudentParams
                .builder()
                .speciality(speciality)
                .build());

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

        CheckResponse firstCheckResponse = sendCheckTaskRequest(studentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(firstCheckResponse.isSuccess());
        Assertions.assertEquals(100, firstCheckResponse.getMark());

        taskId = 2;

        code = """
                public class PlanetApp {
                     public static void main(String[] args) {
                         System.out.println("Mars");
                     }
                 }
                """;

        CheckResponse secondCheckResponse = sendCheckTaskRequest(studentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(secondCheckResponse.isSuccess());
        Assertions.assertEquals(100, secondCheckResponse.getMark());

        GetStudentTaskResultsBySpecialityResponse response = sendGetStudentTaskResultsBySpecialityRequest(
                adminUsername,
                year,
                courseId,
                subjectId,
                speciality);

        User firstStudent = userService.getById(studentUsername);

        List<GetStudentTaskResultsBySpecialityResponse.StudentData> studentDataList = new ArrayList<>(List.of(
                new GetStudentTaskResultsBySpecialityResponse.StudentData(
                        firstStudent.getLastName() + " " + firstStudent.getFirstName(),
                        List.of(
                                new GetStudentTaskResultsBySpecialityResponse.StudentData.PracticeIdTaskIdMarks(
                                        1,
                                        List.of(
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(1, 100),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(2, 100),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(3, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(4, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(5, 0))),
                                new GetStudentTaskResultsBySpecialityResponse.StudentData.PracticeIdTaskIdMarks(
                                        2,
                                        List.of(
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(1, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(2, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(3, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(4, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(5, 0)))
                        ),
                        20)));

        studentDataList.sort(Comparator.comparing(GetStudentTaskResultsBySpecialityResponse.StudentData::getStudentDetails));

        GetStudentTaskResultsBySpecialityResponse expected = GetStudentTaskResultsBySpecialityResponse.success(studentDataList);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that admin can get student task results by speciality sorted by student details")
    @Test
    public void testThatAdminCanGetStudentTaskResultsBySpecialitySortedByStudentDetails() {
        int speciality = 121;

        String adminUsername = userHelperService.generateAdmin();
        String firstStudentUsername = userHelperService.generateStudent(UserHelperService.StudentParams
                .builder()
                .firstName("John")
                .lastName("Doe")
                .speciality(speciality)
                .build());

        String secondStudentUsername = userHelperService.generateStudent(UserHelperService.StudentParams
                .builder()
                .firstName("Michael")
                .lastName("BigFoot")
                .speciality(speciality)
                .build());

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

        CheckResponse firstCheckResponse = sendCheckTaskRequest(firstStudentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(firstCheckResponse.isSuccess());
        Assertions.assertEquals(100, firstCheckResponse.getMark());

        CheckResponse secondCheckResponse = sendCheckTaskRequest(secondStudentUsername, CheckRequest
                .builder()
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .code(code)
                .build());

        Assertions.assertTrue(secondCheckResponse.isSuccess());
        Assertions.assertEquals(100, secondCheckResponse.getMark());

        GetStudentTaskResultsBySpecialityResponse response = sendGetStudentTaskResultsBySpecialityRequest(
                adminUsername,
                year,
                courseId,
                subjectId,
                speciality);

        User firstStudent = userService.getById(firstStudentUsername);
        User secondStudent = userService.getById(secondStudentUsername);

        List<GetStudentTaskResultsBySpecialityResponse.StudentData> studentDataList = new ArrayList<>(List.of(
                new GetStudentTaskResultsBySpecialityResponse.StudentData(
                        secondStudent.getLastName() + " " + secondStudent.getFirstName(),
                        List.of(
                                new GetStudentTaskResultsBySpecialityResponse.StudentData.PracticeIdTaskIdMarks(
                                        1,
                                        List.of(
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(1, 100),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(2, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(3, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(4, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(5, 0))),
                                new GetStudentTaskResultsBySpecialityResponse.StudentData.PracticeIdTaskIdMarks(
                                        2,
                                        List.of(
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(1, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(2, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(3, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(4, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(5, 0)))
                        ),
                        10),
                new GetStudentTaskResultsBySpecialityResponse.StudentData(
                        firstStudent.getLastName() + " " + firstStudent.getFirstName(),
                        List.of(
                                new GetStudentTaskResultsBySpecialityResponse.StudentData.PracticeIdTaskIdMarks(
                                        1,
                                        List.of(
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(1, 100),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(2, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(3, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(4, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(5, 0))),
                                new GetStudentTaskResultsBySpecialityResponse.StudentData.PracticeIdTaskIdMarks(
                                        2,
                                        List.of(
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(1, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(2, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(3, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(4, 0),
                                                new GetStudentTaskResultsBySpecialityResponse.StudentData.TaskIdMark(5, 0)))
                        ),
                        10)
        ));

        studentDataList.sort(Comparator.comparing(GetStudentTaskResultsBySpecialityResponse.StudentData::getStudentDetails));

        GetStudentTaskResultsBySpecialityResponse expected = GetStudentTaskResultsBySpecialityResponse.success(studentDataList);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that not admin can not get student task results by speciality")
    @Test
    public void testThatNotAdminCanNotGetStudentTaskResultsBySpeciality() {
        int speciality = 121;

        String studentUsername = userHelperService.generateStudent();
        String anotherStudentUsername = userHelperService.generateStudent(UserHelperService.StudentParams
                .builder()
                .speciality(speciality)
                .build());

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

        GetStudentTaskResultsBySpecialityResponse response = sendGetStudentTaskResultsBySpecialityRequest(
                studentUsername,
                year,
                courseId,
                subjectId,
                speciality);

        GetStudentTaskResultsBySpecialityResponse expected = GetStudentTaskResultsBySpecialityResponse.failed(
                GetStudentTaskResultsBySpecialityResponse.Error.insufficientPrivileges);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that non-authorized user can not get student task results by speciality")
    @Test
    public void testThatNonAuthorizedUserCanNotGetStudentTaskResultsBySpeciality() {
        int speciality = 121;

        String nonAuthorizedUsername = "NON-AUTHORIZED-USER";

        String studentUsername = userHelperService.generateStudent(UserHelperService.StudentParams
                .builder()
                .speciality(speciality)
                .build());

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

        GetStudentTaskResultsBySpecialityResponse response = sendGetStudentTaskResultsBySpecialityRequest(
                nonAuthorizedUsername,
                year,
                courseId,
                subjectId,
                speciality);

        GetStudentTaskResultsBySpecialityResponse expected = GetStudentTaskResultsBySpecialityResponse.failed(
                GetStudentTaskResultsBySpecialityResponse.Error.invalidCredentials);

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

    private GetStudentTaskResultsBySpecialityResponse sendGetStudentTaskResultsBySpecialityRequest(
            String adminUsername, int year, int courseId, int subjectId, int speciality) {
        String accessToken = jwtService.generateToken(adminUsername);

        if (!userService.exists(adminUsername)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String sendCheckTaskRequestLink = "http://localhost:" + port +
                "/getStudentTaskResultsBySpeciality?year={1}&courseId={2}&subjectId={3}&speciality={4}";

        ResponseEntity<GetStudentTaskResultsBySpecialityResponse> responseEntity = restTemplate.exchange(
                sendCheckTaskRequestLink, HttpMethod.GET, entity, GetStudentTaskResultsBySpecialityResponse.class, year, courseId, subjectId, speciality);

        GetStudentTaskResultsBySpecialityResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCodeValue = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCodeValue, responseEntity.getStatusCode().value());

        return response;
    }
}
