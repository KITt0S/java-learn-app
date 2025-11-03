package com.k1ts.task.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListPassedTasksResponse {
    private boolean success;

    private Error error;

    private Map<Integer, List<Integer>> passedTasks;

    public static ListPassedTasksResponse success(Map<Integer, List<Integer>> passedTasks) {
        return builder()
                .success(true)
                .error(Error.ok)
                .passedTasks(passedTasks)
                .build();
    }

    public static ListPassedTasksResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        ok,
        invalidCredentials,
        invalidStudentUsername, insufficientPrivileges
    }
}
