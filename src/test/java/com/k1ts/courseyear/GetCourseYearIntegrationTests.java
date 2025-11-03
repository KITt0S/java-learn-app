package com.k1ts.courseyear;

import com.k1ts.courseyear.request.GetCourseYearResponse;
import com.k1ts.helper.UserHelperService;
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
public class GetCourseYearIntegrationTests {
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

    // TODO Test that student can get year of course
    @DisplayName("Test that student can get year of course")
    @Test
    public void testThatStudentCanGetYearOfCourse() {
        String studentUsername = userHelperService.generateStudent();

        int courseId = 4;

        GetCourseYearResponse response = sendGetYearOfCourseRequest(studentUsername, courseId);

        GetCourseYearResponse expected = GetCourseYearResponse.success(2023 + 4 - 1);

        Assertions.assertEquals(expected, response);
    }

    // TODO Test that not student can not get year of course
    @DisplayName("Test that not student can not get year of course")
    @Test
    public void testThatNotStudentCanNotGetYearOfCourse() {
        String adminUsername = userHelperService.generateAdmin();

        int courseId = 4;

        GetCourseYearResponse response = sendGetYearOfCourseRequest(adminUsername, courseId);

        GetCourseYearResponse expected = GetCourseYearResponse.failed(GetCourseYearResponse.Error.insufficientPrivileges);

        Assertions.assertEquals(expected, response);
    }

    // TODO Test that non authorized user can not get year of course
    @DisplayName("Test that non authorized user can not get year of course")
    @Test
    public void testThatNonAuthorizedUserCanNotGetYearOfCourse() {
        String nonAuthorizedUsername = "NON-AUTHORIZED-USER";

        int courseId = 4;

        GetCourseYearResponse response = sendGetYearOfCourseRequest(nonAuthorizedUsername, courseId);

        GetCourseYearResponse expected = GetCourseYearResponse.failed(GetCourseYearResponse.Error.invalidCredentials);

        Assertions.assertEquals(expected, response);
    }

    private GetCourseYearResponse sendGetYearOfCourseRequest(String studentUsername, int courseId) {
        String accessToken = jwtService.generateToken(studentUsername);

        if (!userService.exists(studentUsername)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String getYearOfCourseLink = "http://localhost:" + port + "/getYearOfCourse?courseId={1}";

        ResponseEntity<GetCourseYearResponse> responseEntity = restTemplate.exchange(
                getYearOfCourseLink, HttpMethod.GET, entity, GetCourseYearResponse.class, courseId);

        GetCourseYearResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCodeValue = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCodeValue, responseEntity.getStatusCode().value());

        return response;
    }
}
