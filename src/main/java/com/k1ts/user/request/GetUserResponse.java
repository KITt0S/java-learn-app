package com.k1ts.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetUserResponse {
    private boolean success;

    private Error error;

    private String username;

    private String firstName;

    private String lastName;

    public static GetUserResponse success(String username, String firstName, String lastName) {
        return builder()
                .success(true)
                .error(Error.ok)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    public static GetUserResponse failed(Error error) {
        return builder()
                .success(false)
                .error(error)
                .build();
    }

    public enum Error {
        ok,
        invalidCredentials, invalidUsername
    }
}
