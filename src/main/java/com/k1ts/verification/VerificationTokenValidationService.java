package com.k1ts.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VerificationTokenValidationService {
    private final VerificationTokenService verificationTokenService;

    public boolean isTokenInvalid(String tokenId) {
        VerificationToken token = verificationTokenService.getById(tokenId);

        if (token == null) {
            return true;
        }

        return System.currentTimeMillis() > token.getExpiredAt();
    }
}
