package com.k1ts.password;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserRestoreTokenService {
    private final UserRestoreTokenRepository userRestoreTokenRepository;

    public Set<String> getTokensByUsername(String username) {
        return userRestoreTokenRepository.getTokensByUsername(username);
    }

    public void deleteById(String token) {
        userRestoreTokenRepository.deleteById(token);
    }

    public UserRestoreToken save(UserRestoreToken token) {
        return userRestoreTokenRepository.save(token);
    }

    public UserRestoreToken getById(String token) {
        return userRestoreTokenRepository.findById(token).orElse(null);
    }
}
