package com.k1ts.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccessTokenResponse {

    private boolean success;

    private Error error;

    private String accessToken;

    private String refreshToken;

    public static AccessTokenResponse success(String accessToken) {
        return builder().success(true).error(Error.ok).accessToken(accessToken).build();
    }

    public static AccessTokenResponse success(String accessToken, String refreshToken) {
        return builder().success(true).error(Error.ok).accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public static AccessTokenResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        ok
    }
}
