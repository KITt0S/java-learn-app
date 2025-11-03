package com.k1ts.user.request.verify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VerifyAccountResponse {

    private boolean success;

    private Error error;

    public static VerifyAccountResponse success() {
        return builder().success(true).error(Error.ok).build();
    }

    public static VerifyAccountResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        ok,
        invalidToken
    }
}
