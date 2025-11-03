package com.k1ts.lecture.request.getlecture;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetLectureResponse {
    private boolean success;

    private Error error;

    private String lecture;

    public static GetLectureResponse failed(Error error) {
        return builder()
                .success(false)
                .error(error)
                .build();
    }

    public static GetLectureResponse success(String lecture) {
        return builder()
                .success(true)
                .error(Error.ok)
                .lecture(lecture)
                .build();
    }

    public enum Error {
        invalidCredentials, ok
    }
}
