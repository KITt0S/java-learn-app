package com.k1ts.practice.request.practice_toggle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PracticeToggleRequest {
    private String username;

    private int year;

    private int courseId;

    private int subjectId;

    private boolean allowed;
}
