package com.k1ts.password;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserRestoreTokenValidationService {
    private final UserRestoreTokenService userRestoreTokenService;

    public boolean isTokenInvalid(String token) {
        UserRestoreToken userRestoreToken = userRestoreTokenService.getById(token);

        if (userRestoreToken == null) {
            return true;
        }

        return System.currentTimeMillis() > userRestoreToken.getExpiredAt();
    }
}
