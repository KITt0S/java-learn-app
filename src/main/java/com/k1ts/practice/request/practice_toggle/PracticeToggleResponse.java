package com.k1ts.practice.request.practice_toggle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PracticeToggleResponse {

    private boolean success;

    private Error error;

    private boolean allowed;

    public static PracticeToggleResponse success(boolean allowed) {
        return builder().success(true).error(Error.ok).allowed(allowed).build();
    }

    public static PracticeToggleResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        ok,
        insufficientPrivileges,
        invalidCredentials
    }
}
