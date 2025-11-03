package com.k1ts.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User getById(String username) {
        return userRepository.findById(username).orElse(null);
    }

    public boolean exists(String username) {
        return userRepository.existsById(username);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public boolean isEmpty() {
        return userRepository.count() == 0;
    }
}
