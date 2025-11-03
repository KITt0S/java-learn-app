package com.k1ts.security;

import com.k1ts.user.User;
import com.k1ts.user.UserService;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            User user = userService.getById(username);

            if (user == null) {
                throw new UsernameNotFoundException(username);
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList());
        } catch (UsernameNotFoundException e) {
            Sentry.captureException(e);
            throw e;
        }
    }
}
