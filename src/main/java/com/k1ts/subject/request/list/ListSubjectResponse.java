package com.k1ts.subject.request.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListSubjectResponse {

    private boolean success;

    private Error error;

    private Map<Integer, String> subjects;

    public static ListSubjectResponse failed(Error error) {
        return builder()
                .success(false)
                .error(error)
                .build();
    }

    public static ListSubjectResponse success(Map<Integer, String> subjects) {
        return builder()
                .success(true)
                .error(Error.ok)
                .subjects(subjects)
                .build();
    }

    public enum Error {
        ok,
        invalidYear,
        invalidCourse,
        invalidSpeciality,
        insufficientPrivileges,
        invalidCredentials
    }
}
