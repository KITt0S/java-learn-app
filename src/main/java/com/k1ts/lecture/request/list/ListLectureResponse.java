package com.k1ts.lecture.request.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListLectureResponse {
    private boolean success;

    private int lectureCount;

    private Error error;

    public static ListLectureResponse failed(Error error) {
        return builder()
                .success(false)
                .error(error)
                .build();
    }

    public static ListLectureResponse success(int lectureCount) {
        return builder()
                .success(true)
                .error(Error.ok)
                .lectureCount(lectureCount)
                .build();
    }

    public enum Error {
        invalidCredentials, noLectures, ok
    }
}
