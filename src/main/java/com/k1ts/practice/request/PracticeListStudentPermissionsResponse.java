package com.k1ts.practice.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PracticeListStudentPermissionsResponse {

    private boolean success;

    private Error error;

    private List<StudentPracticePermission> permissions;

    public static PracticeListStudentPermissionsResponse success(List<StudentPracticePermission> permissions) {
        return builder().success(true).error(Error.ok).permissions(permissions).build();
    }

    public static PracticeListStudentPermissionsResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        ok,
        invalidCredentials,
        insufficientPrivileges
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class StudentPracticePermission {

        private String username;

        private String lastName;

        private String firstName;

        private boolean allowed;
    }
}
