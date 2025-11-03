package com.k1ts.task;

import com.k1ts.helper.UserHelperService;
import com.k1ts.security.JwtService;
import com.k1ts.security.SecurityConstants;
import com.k1ts.task.request.ListPracticesResponse;
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
public class ListPracticesIntegrationTests {
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

    @DisplayName("Test that user can list practices")
    @Test
    public void testThatUserCanListPractices() {
        String username = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;

        ListPracticesResponse response = sendListPracticesRequest(username, year, courseId, subjectId);

        ListPracticesResponse expected = ListPracticesResponse.success(2);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that user can not list empty practices")
    @Test
    public void testThatUserCanNotListEmptyPractices() {
        String username = userHelperService.generateUser();

        int year = 2024;
        int courseId = 2;
        int subjectId = 1;

        ListPracticesResponse response = sendListPracticesRequest(username, year, courseId, subjectId);

        ListPracticesResponse expected = ListPracticesResponse.failed(ListPracticesResponse.Error.noPractices);

        Assertions.assertEquals(expected, response);
    }

    @DisplayName("Test that non-authorized user can not list practices")
    @Test
    public void testThatNonAuthorizedUserCanNotListPractices() {
        String username = "NON-AUTHORIZED-USER";

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;

        ListPracticesResponse response = sendListPracticesRequest(username, year, courseId, subjectId);

        ListPracticesResponse expected = ListPracticesResponse.failed(ListPracticesResponse.Error.invalidCredentials);

        Assertions.assertEquals(expected, response);
    }

    private ListPracticesResponse sendListPracticesRequest(String username, int year, int courseId, int subjectId) {
        String accessToken = jwtService.generateToken(username);

        if (!userService.exists(username)) {
            accessToken = "INVALID_TOKEN";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String listPracticesLink = "http://localhost:" + port + "/listPractices?year={1}&courseId={2}&subjectId={3}";

        ResponseEntity<ListPracticesResponse> responseEntity = restTemplate.exchange(
                listPracticesLink, HttpMethod.GET, entity, ListPracticesResponse.class, year, courseId, subjectId);

        ListPracticesResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);

        int expectedStatusCodeValue = response.getError().name().equals("invalidCredentials") ? 401 : 200;

        Assertions.assertEquals(expectedStatusCodeValue, responseEntity.getStatusCode().value());

        return response;
    }
}
