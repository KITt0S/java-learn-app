package com.k1ts.password.request.restore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PasswordRestoreResponse {

    private boolean success;

    private Error error;

    public static PasswordRestoreResponse success() {
        return builder().success(true).error(Error.ok).build();
    }

    public static PasswordRestoreResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        ok,
        usernameNotExists,
        invalidCredentials
    }
}
