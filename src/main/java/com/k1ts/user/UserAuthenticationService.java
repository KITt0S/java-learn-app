package com.k1ts.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.k1ts.email.EmailService;
import com.k1ts.helper.StringCleaner;
import com.k1ts.security.JwtService;
import com.k1ts.security.request.JwtAuthenticationResponse;
import com.k1ts.user.request.*;
import com.k1ts.user.request.verify.VerifyAccountResponse;
import com.k1ts.user.studentdata.StudentData;
import com.k1ts.user.studentdata.StudentFirstCourseDateDefiner;
import com.k1ts.user.studentdata.StudentDataService;
import com.k1ts.verification.VerificationToken;
import com.k1ts.verification.VerificationTokenConstants;
import com.k1ts.verification.VerificationTokenService;
import com.k1ts.verification.VerificationTokenValidationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RequiredArgsConstructor
@Service
public class UserAuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserValidationService userValidationService;
    private final StudentDataService studentDataService;
    private final StudentFirstCourseDateDefiner studentFirstCourseDateDefiner;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final VerificationTokenValidationService verificationTokenValidationService;
    private final GoogleCredentialsManager googleCredentialsManager;

    @Value(value = "${frontendDomain:}")
    private String frontendDomain;

    public JwtAuthenticationResponse signup(SignUpRequest request) {
        String email = StringCleaner.clean(request.getEmail());

        if (userValidationService.isEmailInvalid(email)) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidEmail);
        }

        if (userService.exists(email)) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.userAlreadyExists);
        }

        String firstName = StringCleaner.clean(request.getFirstName());

        if (userValidationService.isFirstNameInvalid(firstName)) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidFirstName);
        }

        String patronymic = StringCleaner.clean(request.getPatronymic());

        if (userValidationService.isPatronymicInvalid(patronymic)) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidPatronymic);
        }

        String lastName = StringCleaner.clean(request.getLastName());

        if (userValidationService.isLastNameInvalid(lastName)) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidLastName);
        }

        String password = StringCleaner.clean(request.getPassword());

        if (userValidationService.isPasswordInvalid(password)) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidPassword);
        }

        if (userValidationService.isCourseNumberInvalid(request.getCourseNumber())) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidCourseNumber);
        }

        if (userValidationService.isSpecialityInvalid(request.getSpeciality())) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidSpeciality);
        }

        User user = userService.save(User
                .builder()
                .username(email)
                .role(Role.Student)
                .firstName(firstName)
                .patronymic(patronymic)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build());

        StudentData studentData = studentDataService.getByUsername(user.getUsername());

        if (studentData == null) {
            studentDataService.save(StudentData
                    .builder()
                    .student(user)
                    .firstCourseDate(studentFirstCourseDateDefiner.defineFirstCourseDate(request.getCourseNumber()))
                    .speciality(request.getSpeciality())
                    .build());
        } else {
            studentData.setFirstCourseDate(studentFirstCourseDateDefiner.defineFirstCourseDate(request.getCourseNumber()));
            studentData.setSpeciality(request.getSpeciality());
            studentDataService.save(studentData);
        }

        createVerificationTokenAndSendEmail(email);

        String token = jwtService.generateToken(user.getUsername());

        return JwtAuthenticationResponse.success(token);
    }

    private void createVerificationTokenAndSendEmail(String email) {
        deletePreviousTokens(email);

        VerificationToken token = createVerificationToken(email);

        sendEmail(token);
    }

    private void deletePreviousTokens(String email) {
        for (String token : verificationTokenService.getTokensByUsername(email)) {
            verificationTokenService.deleteById(token);
        }
    }

    private VerificationToken createVerificationToken(String email) {
        VerificationToken token = new VerificationToken();
        token.setExpiredAt(System.currentTimeMillis() + VerificationTokenConstants.EXPIRED_AT_MILLISECONDS);
        token.setUser(userService.getById(email));

        return verificationTokenService.save(token);
    }

    private void sendEmail(VerificationToken token) {
        emailService.sendEmail("noreply@exampleapp.online",
                "Підтвердження email - Java Learn App",
                token.getUser().getUsername(),
                "Посилання на підтвердження email\n"
                        + "\n"
                        + frontendDomain +
                        "/email_verification.html?token=" + token.getToken());
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
        } catch (Exception e) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.invalidEmailOrPassword);
        }

        User user = userService.getById(request.getEmail());

        if (!user.isVerified()) {
            return JwtAuthenticationResponse.failed(JwtAuthenticationResponse.Error.notVerifiedUser);
        }

        String token = jwtService.generateToken(user.getUsername());

        return JwtAuthenticationResponse.success(token);
    }

    public VerifyAccountResponse verifyAccount(String tokenId) {
        if (verificationTokenValidationService.isTokenInvalid(tokenId)) {
            return VerifyAccountResponse.failed(VerifyAccountResponse.Error.invalidToken);
        }

        User user = verificationTokenService.getById(tokenId).getUser();

        user.setVerified(true);
        userService.save(user);

        verificationTokenService.deleteById(tokenId);

        return VerifyAccountResponse.success();
    }

    public AccessTokenResponse callback(HttpServletRequest request, HttpServletResponse response) {
        String authorizationCode = request.getParameter("code");

        if (authorizationCode != null) {
            try {
                GoogleAuthorizationCodeTokenRequest secondRequest = new GoogleAuthorizationCodeTokenRequest(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),
                        googleCredentialsManager.getClientId(),
                        googleCredentialsManager.getClientSecret(),
                        authorizationCode,
                        googleCredentialsManager.getRedirectUri());

                GoogleTokenResponse tokenResponse = secondRequest.execute();

                String accessToken = tokenResponse.getAccessToken();

                // Створення HTTP клієнта
                HttpTransport transport = new NetHttpTransport();
                HttpRequestFactory requestFactory = transport.createRequestFactory();

                // URL для доступу до інформації користувача
                GenericUrl url = new GenericUrl("https://www.googleapis.com/oauth2/v3/userinfo");

                // Створення запиту GET з додаванням токена доступу до заголовків
                HttpRequest thirdRequest = requestFactory.buildGetRequest(url);
                thirdRequest.getHeaders().setAuthorization("Bearer " + accessToken);

                // Виконання запиту
                HttpResponse thirdResponse = thirdRequest.execute();

                UserInfo userInfo = new Gson().fromJson(thirdResponse.parseAsString(), UserInfo.class);

                response.sendRedirect("https://js-java-learn-app.vercel.app/callback.html?token=" + jwtService.generateToken(userInfo.getEmail()));
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
