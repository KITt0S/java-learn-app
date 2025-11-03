package com.k1ts.check.request.javatask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JavaTaskCheckResponse {

    private boolean success;

    private Error error;

    private List<String> successResult;

    private List<String> failedResult;

    private String codeResult;

    private String errorResult;

    private boolean taskPassed;

    public static JavaTaskCheckResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        ok,
        noSuchTaskService,
        wrongCodeDetected,
        invalidToken
    }
}
