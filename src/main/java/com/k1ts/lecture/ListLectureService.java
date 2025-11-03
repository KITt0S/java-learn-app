package com.k1ts.lecture;

import com.k1ts.lecture.request.list.ListLectureResponse;
import com.k1ts.subject.SubjectIdDefiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ListLectureService {
    private final SubjectIdDefiner subjectIdDefiner;

    public ListLectureResponse list(int year, int courseId, int subjectId) {
        String lecturesPath = "materials/" + year + "/" + courseId + "_course/" +
                subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) + "/lectures";

        File[] files = new File(lecturesPath).listFiles();

        if (files == null) {
            return ListLectureResponse.failed(ListLectureResponse.Error.noLectures);
        }

        return ListLectureResponse.success(files.length);
    }
}

