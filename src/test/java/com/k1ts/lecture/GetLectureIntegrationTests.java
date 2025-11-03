package com.k1ts.lecture;

import com.k1ts.helper.UserHelperService;
import com.k1ts.lecture.request.getlecture.GetLectureResponse;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.subject.SubjectIdDefiner;
import com.k1ts.user.UserService;
import io.sentry.Sentry;
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

import java.io.*;
import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetLectureIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserHelperService userHelperService;

    @Autowired
    private SubjectIdDefiner subjectIdDefiner;

    @DisplayName("Test that authorized user can get lecture")
    @Test
    public void testThatAuthorizedUserCanGetLecture() {
        String username = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 1;
        int lectureId = 1;

        GetLectureResponse response = sendGetLectureRequest(username, year, courseId, subjectId, lectureId);

        String filePath = "materials/" + year + "/" + courseId + "_course/" + subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) +
                "/lectures/lecture_" + lectureId + ".md";

        StringBuilder lectureBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {

            String textLine;
            while ((textLine = reader.readLine()) != null) {
                lectureBuilder.append(textLine).append("\n");
            }
        } catch (IOException e) {
            Sentry.captureException(e);
            throw new RuntimeException(e);
        }

        GetLectureResponse expectedResponse = GetLectureResponse
                .builder()
                .success(true)
                .error(GetLectureResponse.Error.ok)
                .lecture(lectureBuilder.toString())
                .build();

        Assertions.assertEquals(expectedResponse, response);
    }

    @DisplayName("Test that non-authorized user can not get lecture")
    @Test
    public void testThatNoAuthorizedUserCanNotGetLecture() {
        String username = "NON_AUTHORIZED_USER";

        int year = 2024;
        int courseId = 2;
        int subjectId = 1;
        int lectureId = 1;

        GetLectureResponse response = sendGetLectureRequest(username, year, courseId, subjectId, lectureId);

        GetLectureResponse expected = GetLectureResponse.failed(GetLectureResponse.Error.invalidCredentials);

        Assertions.assertEquals(expected, response);
    }

    private GetLectureResponse sendGetLectureRequest(String username, int year, int courseNumber, int subjectId, int lectureNumber) {
        String accessToken = jwtService.generateToken(username);

        if (!userService.exists(username)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String getLectureLink =
                "http://localhost:" + port + "/lecture/getLecture?year={1}&courseId={2}&subjectId={3}&lectureId={4}";

        ResponseEntity<GetLectureResponse> responseEntity = restTemplate.exchange(
                getLectureLink, HttpMethod.GET, entity, GetLectureResponse.class, year, courseNumber, subjectId, lectureNumber);

        GetLectureResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCode = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCode, responseEntity.getStatusCode().value());

        return response;
    }
}
