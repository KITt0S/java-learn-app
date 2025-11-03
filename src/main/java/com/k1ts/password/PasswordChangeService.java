package com.k1ts.password;

import com.k1ts.password.request.change.PasswordChangeResponse;
import com.k1ts.password.request.restore.PasswordRestoreResponse;
import com.k1ts.user.User;
import com.k1ts.user.UserService;
import com.k1ts.user.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordChangeService {
    private final UserRestoreTokenService userRestoreTokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRestoreTokenValidationService userRestoreTokenValidationService;
    private final UserValidationService userValidationService;

    public PasswordChangeResponse changePassword(String token, String password) {
        if (userRestoreTokenValidationService.isTokenInvalid(token)) {
            return PasswordChangeResponse.failed(PasswordChangeResponse.Error.invalidToken);
        }

        if (userValidationService.isPasswordInvalid(password)) {
            return PasswordChangeResponse.failed(PasswordChangeResponse.Error.invalidPassword);
        }

        UserRestoreToken userRestoreToken = userRestoreTokenService.getById(token);
        User user = userRestoreToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userService.save(user);

        userRestoreTokenService.deleteById(token);

        return PasswordChangeResponse.success();
    }
}
