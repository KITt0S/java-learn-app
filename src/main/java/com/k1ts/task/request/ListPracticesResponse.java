package com.k1ts.task.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListPracticesResponse {
    private boolean success;

    private Error error;

    private int practiceCount;

    public static ListPracticesResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public static ListPracticesResponse success(int count) {
        return builder().success(true).error(Error.ok).practiceCount(count).build();
    }

    public enum Error {
        ok,
        noPractices,
        invalidCredentials
    }
}
