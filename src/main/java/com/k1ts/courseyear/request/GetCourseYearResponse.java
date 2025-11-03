package com.k1ts.courseyear.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetCourseYearResponse {
    private boolean success;

    private Error error;

    private int year;

    public static GetCourseYearResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public static GetCourseYearResponse success(int year) {
        return builder().success(true).error(Error.ok).year(year).build();
    }

    public enum Error {
        ok,
        insufficientPrivileges, invalidCredentials
    }
}
