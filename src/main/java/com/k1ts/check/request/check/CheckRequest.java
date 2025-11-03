package com.k1ts.check.request.check;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CheckRequest {

    private int year;

    private int courseId;

    private int subjectId;

    private int practiceId;

    private int taskId;

    private String code;
}
