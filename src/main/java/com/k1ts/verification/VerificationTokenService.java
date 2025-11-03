package com.k1ts.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    @Query(nativeQuery = true, value = "SELECT token FROM verification_token WHERE user_id = :username")
    public Set<String> getTokensByUsername(String username) {
        return verificationTokenRepository.getTokensByUsername(username);
    }

    public VerificationToken save(VerificationToken token) {
        return verificationTokenRepository.save(token);
    }

    public void deleteById(String token) {
        verificationTokenRepository.deleteById(token);
    }

    public VerificationToken getById(String token) {
        return verificationTokenRepository.findById(token).orElse(null);
    }

    public boolean existsById(String s) {
        return verificationTokenRepository.existsById(s);
    }
}
