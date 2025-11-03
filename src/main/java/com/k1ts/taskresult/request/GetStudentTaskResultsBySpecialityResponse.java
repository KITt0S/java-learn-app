package com.k1ts.taskresult.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetStudentTaskResultsBySpecialityResponse {
    private boolean success;

    private Error error;

    private List<StudentData> studentPractices;

    public static GetStudentTaskResultsBySpecialityResponse success(List<StudentData> studentData) {
        return builder().success(true).error(Error.ok).studentPractices(studentData).build();
    }

    public static GetStudentTaskResultsBySpecialityResponse failed(Error error) {
        return builder().success(false).error(error).build();
    }

    public enum Error {
        ok,
        insufficientPrivileges,
        invalidCredentials
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class StudentData {
        private String studentDetails;

        private List<PracticeIdTaskIdMarks> practiceIdTaskResults;

        private int averageMark;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class PracticeIdTaskIdMarks {
            private int practiceId;

            private List<TaskIdMark> taskIdMarks;
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class TaskIdMark {
            private int taskId;
            private int mark;
        }
    }
}
