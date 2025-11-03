package com.k1ts.user;

import com.k1ts.security.request.JwtAuthenticationResponse;
import com.k1ts.user.request.*;
import com.k1ts.user.request.verify.VerifyAccountRequest;
import com.k1ts.user.request.verify.VerifyAccountResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserAuthenticationService userAuthenticationService;
    private final GetUserDateService getUserDateService;
    private final UserService userService;

    @PostMapping("/auth/signup")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return userAuthenticationService.signup(request);
    }

    @PostMapping("/auth/signIn")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest request) {
        return userAuthenticationService.signIn(request);
    }

    @GetMapping("/auth/callback")
    public AccessTokenResponse callback(HttpServletRequest request, HttpServletResponse response) {
        return userAuthenticationService.callback(request, response);
    }

    @PostMapping("/auth/verify")
    public VerifyAccountResponse verifyAccount(@RequestBody VerifyAccountRequest request) {
        return userAuthenticationService.verifyAccount(request.getToken());
    }

    @GetMapping("/getUserData")
    public GetUserResponse user(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return GetUserResponse.failed(GetUserResponse.Error.invalidCredentials);
        }

        return getUserDateService.getUserData(principal.getName());
    }
}
