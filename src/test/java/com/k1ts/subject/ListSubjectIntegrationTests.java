package com.k1ts.subject;

import com.k1ts.helper.UserHelperService;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.subject.request.list.ListSubjectResponse;
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

import java.util.Map;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListSubjectIntegrationTests {

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

    @DisplayName("Test that authorized user can list subjects")
    @Test
    public void testThatAuthorizedUserCanListSubjects() {
        String username = userHelperService.generateUser();

        int year = 2024;
        int courseId = 2;

        ListSubjectResponse response = sendListSubjectRequest(username, year, courseId);

        ListSubjectResponse expected = ListSubjectResponse.success(Map.of(
                1, "Java-програмування",
                2, "Об'єктно-орієнтоване програмування"));

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that non-authorized user can not list subjects")
    @Test
    public void testThatNonAuthorizedCanNotListSubjects() {
        String username = "NON-AUTHORIZED-USER";

        int year = 2024;
        int courseId = 2;

        ListSubjectResponse response = sendListSubjectRequest(username, year, courseId);

        ListSubjectResponse expected = ListSubjectResponse.failed(ListSubjectResponse.Error.invalidCredentials);

        Assertions.assertEquals(expected, response);
    }

    private ListSubjectResponse sendListSubjectRequest(String username, int year, int courseId) {
        String accessToken = jwtService.generateToken(username);

        if (!userService.exists(username)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String listSubjectLink = "http://localhost:" + port + "/subject/list?year={1}&courseId={2}";

        ResponseEntity<ListSubjectResponse> responseEntity = restTemplate.exchange(
                listSubjectLink, HttpMethod.GET, entity, ListSubjectResponse.class, year, courseId);

        ListSubjectResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCode = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCode, responseEntity.getStatusCode().value());

        return response;
    }
}
