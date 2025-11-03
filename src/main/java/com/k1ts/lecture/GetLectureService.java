package com.k1ts.lecture;

import com.k1ts.lecture.request.getlecture.GetLectureResponse;
import com.k1ts.subject.SubjectIdDefiner;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class GetLectureService {
    private final SubjectIdDefiner subjectIdDefiner;

    public GetLectureResponse getLecture(int year, int courseId, int subjectId, int lectureId) {

        String filePath = "materials/" + year + "/" + courseId + "_course/" + subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) +
                "/lectures/lecture_" + lectureId + ".md";

        StringBuilder lectureBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {

            String textLine;

            while ((textLine = reader.readLine()) != null) {
                lectureBuilder.append(textLine).append("\n");
            }
        } catch (IOException e) {
            Sentry.captureException(e);
            throw new RuntimeException(e);
        }

        return GetLectureResponse.success(lectureBuilder.toString());
    }
}
