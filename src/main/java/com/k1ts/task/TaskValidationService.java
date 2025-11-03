package com.k1ts.task;

import com.k1ts.subject.SubjectIdDefiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TaskValidationService {
    private final SubjectIdDefiner subjectIdDefiner;

    public boolean isInvalidCourseId(int courseId) {
        if (courseId < TaskConstants.MIN_COURSE_NUMBER) {
            return true;
        }

        return courseId > TaskConstants.MAX_COURSE_NUMBER;
    }

    public boolean isInvalidSubjectId(int year, int courseId, int subjectId) {
        if (subjectId < TaskConstants.MIN_SUBJECT_ID) {
            return true;
        }

        File[] files = new File("materials/" + year + "/" + courseId + "_course/").listFiles();

        if (files == null) {
            return true;
        }

        return subjectId > files.length;
    }

    public boolean isInvalidPracticeId(int year, int courseId, int subjectId, int practiceId) {
        if (practiceId < TaskConstants.MIN_PRACTICE_ID) {
             return true;
        }

        File[] files = new File("materials/" + year + "/" + courseId + "_course/" +
                subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) +
                "/practices/").listFiles();

        if (files == null) {
            return true;
        }

        return practiceId > files.length;
    }

    public boolean isInvalidTaskId(int year, int courseId, int subjectId, int practiceId, int taskId) {
        if (taskId < TaskConstants.MIN_TASK_ID) {
            return true;
        }

        File[] files = new File("materials/" + year + "/" + courseId + "_course/" +
                subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) +
                "/practices/" + "practice_" + practiceId).listFiles();

        if (files == null) {
            return true;
        }

        return taskId > files.length;
    }
}
