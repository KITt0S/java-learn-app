package com.k1ts.taskresult.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetStudentTaskResultsResponse {
    private boolean success;

    private Error error;

    private Map<Integer, Map<Integer, Integer>> practiceIdTaskMarksMap;

    private int averageMark;

    public enum Error {
        ok,
        insufficientPrivileges,
        invalidCredentials
    }

    public static GetStudentTaskResultsResponse success(Map<Integer, Map<Integer, Integer>> practiceIdTaskMarksMap, int averageMark) {
        return builder()
                .success(true)
                .error(Error.ok)
                .practiceIdTaskMarksMap(practiceIdTaskMarksMap)
                .averageMark(averageMark)
                .build();
    }

    public static GetStudentTaskResultsResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    @AllArgsConstructor
    public static class TaskMarksClass {
        private final Map<Integer, Integer> taskIdMarkMap;
        private final int averageMark;
    }
}
