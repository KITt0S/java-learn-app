package com.k1ts.task.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListTasksResponse {
    private boolean success;

    private Error error;

    private int taskCount;

    public static ListTasksResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public static ListTasksResponse success(int count) {
        return builder().success(true).error(Error.ok).taskCount(count).build();
    }

    public enum Error {
        ok,
        noTasks,
        invalidCredentials
    }
}
