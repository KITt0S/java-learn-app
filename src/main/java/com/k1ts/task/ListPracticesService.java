package com.k1ts.task;

import com.k1ts.subject.SubjectIdDefiner;
import com.k1ts.task.request.ListPracticesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@RequiredArgsConstructor
@Service
public class ListPracticesService {
    private final SubjectIdDefiner subjectIdDefiner;

    public ListPracticesResponse listPractices(int year, int courseId, int subjectId) {

        String practicesPath = "materials/" + year + "/" + courseId + "_course/" +
                subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) + "/practices";

        File file = new File(practicesPath);

        File[] files = file.listFiles();

        if (files == null) {
            return ListPracticesResponse.failed(ListPracticesResponse.Error.noPractices);
        }

        return ListPracticesResponse.success(files.length);
    }
}
