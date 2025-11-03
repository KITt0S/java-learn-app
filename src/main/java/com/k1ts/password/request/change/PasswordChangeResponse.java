package com.k1ts.password.request.change;

import com.k1ts.password.request.restore.PasswordRestoreResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PasswordChangeResponse {

    private boolean success;

    private Error error;

    public static PasswordChangeResponse success() {
        return builder().success(true).error(Error.ok).build();
    }

    public static PasswordChangeResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        invalidPassword,
        invalidToken,
        ok
    }
}
