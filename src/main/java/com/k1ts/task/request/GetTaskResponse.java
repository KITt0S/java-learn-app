package com.k1ts.task.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetTaskResponse {
    private boolean success;

    private Error error;

    private String taskText;

    private String code;

    public static GetTaskResponse failed(Error error) {
        return builder()
                .success(false)
                .error(error)
                .build();
    }

    public enum Error {
        ok,
        invalidPracticeId,
        invalidFile,
        invalidCredentials,
        invalidCourseId,
        invalidSubjectId,
        invalidTaskId
    }
}
