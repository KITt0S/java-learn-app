package com.k1ts.user;

import com.k1ts.user.request.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserDateService {
    private final UserService userService;

    public GetUserResponse getUserData(String username) {

        User user = userService.getById(username);

        return GetUserResponse.success(user.getUsername(), user.getFirstName(), user.getLastName());
    }
}
