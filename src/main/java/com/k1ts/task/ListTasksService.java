package com.k1ts.task;

import com.k1ts.subject.SubjectIdDefiner;
import com.k1ts.task.request.ListTasksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@RequiredArgsConstructor
@Service
public class ListTasksService {
    private final SubjectIdDefiner subjectIdDefiner;

    public ListTasksResponse listTasks(int year, int courseId, int subjectId, int practiceId) {

        String filePath = "materials/"  + year + "/" + courseId + "_course/" +
                subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) + "/practices/practice_" + practiceId;

        File[] files = new File(filePath).listFiles();

        if (files == null) {
            return ListTasksResponse.failed(ListTasksResponse.Error.noTasks);
        }

        return ListTasksResponse.success(files.length);
    }
}
