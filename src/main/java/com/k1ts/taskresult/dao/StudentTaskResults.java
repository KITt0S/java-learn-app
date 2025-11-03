package com.k1ts.taskresult.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class StudentTaskResults {

    private final String studentDetails;

    private final List<PracticeIdTaskIdMarks> practiceIdTaskIdMarks;

    @Data
    public static class PracticeIdTaskIdMarks {

        private final int practiceId;

        private final List<TaskIdMark> taskIdMarks;

    }

    @AllArgsConstructor
    @Data
    public static class TaskIdMark {

        private final int taskId;

        private final int mark;
    }
}
