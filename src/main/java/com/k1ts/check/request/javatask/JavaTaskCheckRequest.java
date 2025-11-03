package com.k1ts.check.request.javatask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JavaTaskCheckRequest {
    private String token;

    private int year;

    private int courseId;

    private int subjectId;

    private int practiceId;

    private int taskId;

    private String code;
}
