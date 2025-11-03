package com.k1ts.password;

import com.k1ts.email.EmailService;
import com.k1ts.password.request.restore.PasswordRestoreResponse;
import com.k1ts.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordRestoreService {
    private final UserService userService;
    private final UserRestoreTokenService userRestoreTokenService;
    private final EmailService emailService;

    @Value("${frontendDomain:}")
    private String frontendDomain;

    public PasswordRestoreResponse restore(String username) {
        if (!userService.exists(username)) {
            return PasswordRestoreResponse.failed(PasswordRestoreResponse.Error.usernameNotExists);
        }

        createTokenAndSendEmail(username);

        return PasswordRestoreResponse.success();
    }

    private void createTokenAndSendEmail(String username) {
        deletePreviousTokens(username);

        UserRestoreToken token = createToken(username);

        sendEmail(token);
    }

    private void sendEmail(UserRestoreToken token) {
        emailService.sendEmail("noreply@exampleapp.online",
                "Відновлення паролю - Java Learn App",
                token.getUser().getUsername(),
                "Посилання на відновлення паролю\n"
                        + "\n"
                        + frontendDomain +
                        "/reset_password.html?token=" + token.getToken());
    }

    private UserRestoreToken createToken(String username) {
        UserRestoreToken token = new UserRestoreToken();
        token.setExpiredAt(System.currentTimeMillis() + UserRestoreTokenConstants.EXPIRED_AT_MILLISECONDS);
        token.setUser(userService.getById(username));

        return userRestoreTokenService.save(token);
    }

    private void deletePreviousTokens(String username) {
        for (String token : userRestoreTokenService.getTokensByUsername(username)) {
            userRestoreTokenService.deleteById(token);
        }
    }
}
