package com.k1ts.practice;

import com.k1ts.check.CheckService;
import com.k1ts.helper.UserHelperService;
import com.k1ts.practice.request.PracticeListStudentPermissionsResponse;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.user.User;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PracticeListStudentPermissionsIntegrationTests {

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

    @DisplayName("Test that admin can get student practice permissions")
    @Test
    public void testThatAdminCanGetStudentPracticePermissions() {
        String adminUsername = userHelperService.generateAdmin();

        String studentUsername = userHelperService.generateStudent();

        User student = userService.getById(studentUsername);

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int speciality = student.getStudentData().getSpeciality();

        PracticeListStudentPermissionsResponse response = sendPracticeGetStudentPermissionsRequest(
                adminUsername, year, courseId, subjectId, speciality);

        PracticeListStudentPermissionsResponse expected =
                PracticeListStudentPermissionsResponse.success(Collections.singletonList(
                        PracticeListStudentPermissionsResponse.StudentPracticePermission
                                .builder()
                                .username(student.getUsername())
                                .lastName(student.getLastName())
                                .firstName(student.getFirstName())
                                .allowed(false)
                                .build()));

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that admin can get sorted student practice permissions")
    @Test
    public void testThatAdminCanGetSortedStudentPracticePermissions() {
        String adminUsername = userHelperService.generateAdmin();

        String firstStudentUsername = userHelperService.generateStudent(UserHelperService.StudentParams
                .builder()
                .lastName("Doe")
                .speciality(121)
                .build());

        String secondStudentUsername = userHelperService.generateStudent(UserHelperService.StudentParams
                .builder()
                .lastName("Bigfoot")
                .speciality(121)
                .build());

        User firstStudent = userService.getById(firstStudentUsername);
        User secondStudent = userService.getById(secondStudentUsername);

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int speciality = firstStudent.getStudentData().getSpeciality();

        PracticeListStudentPermissionsResponse response = sendPracticeGetStudentPermissionsRequest(
                adminUsername, year, courseId, subjectId, speciality);

        PracticeListStudentPermissionsResponse expected =
                PracticeListStudentPermissionsResponse.success(List.of(
                        PracticeListStudentPermissionsResponse.StudentPracticePermission
                                .builder()
                                .username(secondStudent.getUsername())
                                .lastName(secondStudent.getLastName())
                                .firstName(secondStudent.getFirstName())
                                .allowed(false)
                                .build(),
                        PracticeListStudentPermissionsResponse.StudentPracticePermission
                                .builder()
                                .username(firstStudent.getUsername())
                                .lastName(firstStudent.getLastName())
                                .firstName(firstStudent.getFirstName())
                                .allowed(false)
                                .build()));

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that not-admin can not get student practice permissions")
    @Test
    public void testThatNotAdminCanNotGetStudentPracticePermissions() {
        String firstStudentUsername = userHelperService.generateStudent();

        String secondStudentUsername = userHelperService.generateStudent();

        User secondStudent = userService.getById(secondStudentUsername);

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int speciality = secondStudent.getStudentData().getSpeciality();

        PracticeListStudentPermissionsResponse response = sendPracticeGetStudentPermissionsRequest(
                firstStudentUsername, year, courseId, subjectId, speciality);

        PracticeListStudentPermissionsResponse expected = PracticeListStudentPermissionsResponse.failed(
                PracticeListStudentPermissionsResponse.Error.insufficientPrivileges);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that non-authorized user can not get student practice permissions")
    @Test
    public void testThatNonAuthorizedUserCanNotGetStudentPracticePermissions() {
        String nonAuthorizedUser = "NON-AUTHORIZED-USER";

        String studentUsername = userHelperService.generateStudent();

        User student = userService.getById(studentUsername);

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int speciality = student.getStudentData().getSpeciality();

        PracticeListStudentPermissionsResponse response = sendPracticeGetStudentPermissionsRequest(
                nonAuthorizedUser, year, courseId, subjectId, speciality);

        PracticeListStudentPermissionsResponse expected = PracticeListStudentPermissionsResponse.failed(
                PracticeListStudentPermissionsResponse.Error.invalidCredentials);

        Assertions.assertEquals(expected, response);
    }

    private PracticeListStudentPermissionsResponse sendPracticeGetStudentPermissionsRequest(String username, int year, int courseId, int subjectId, int speciality) {
        String accessToken = jwtService.generateToken(username);

        if (!userService.exists(username)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String getStudentPracticePermisssionsLink =
                "http://localhost:" + port + "/practice/getStudentPracticePermissions?year={1}&courseId={2}&subjectId={3}&speciality={4}";

        ResponseEntity<PracticeListStudentPermissionsResponse> responseEntity = restTemplate.exchange(
                getStudentPracticePermisssionsLink, HttpMethod.GET, entity, PracticeListStudentPermissionsResponse.class, year, courseId, subjectId, speciality);

        PracticeListStudentPermissionsResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int statusCode = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(statusCode, responseEntity.getStatusCode().value());

        return response;
    }
}
