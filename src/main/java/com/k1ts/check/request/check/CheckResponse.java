package com.k1ts.check.request.check;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CheckResponse {

    private boolean success;

    private boolean taskPassed;

    private int mark;

    private List<String> successResult;

    private List<String> failedResult;

    private String codeResult;

    private String errorResult;

    private Error error;

    public static CheckResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public static CheckResponse success(int mark) {
        return builder().success(true).error(Error.ok).taskPassed(true).mark(mark).build();
    }

    public enum Error {
        ok,
        invalidCredentials, noSuchTaskService, wrongCodeDetected, practiceIsNotAllowed;
    }
}
