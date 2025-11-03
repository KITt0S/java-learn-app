package com.k1ts.security.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtAuthenticationResponse {
    private boolean success;

    private String token;

    private Error error;

    public static JwtAuthenticationResponse failed(Error error) {
        return builder()
                .success(false)
                .error(error)
                .build();
    }

    public static JwtAuthenticationResponse success(String token) {
        return builder()
                .success(true)
                .error(Error.ok)
                .token(token)
                .build();
    }

    public enum Error {
        ok,
        invalidEmail,
        invalidPassword,
        invalidEmailOrPassword,
        invalidFirstName,
        invalidCourseNumber,
        invalidSpeciality,
        invalidPatronymic,
        userAlreadyExists,
        notVerifiedUser,
        invalidLastName
    }
}