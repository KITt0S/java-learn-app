package com.k1ts.lecture;

import com.k1ts.lecture.request.getlecture.GetLectureResponse;
import com.k1ts.lecture.request.list.ListLectureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/lecture")
@RestController
public class LectureController {
    private final ListLectureService listLectureService;
    private final GetLectureService getLectureService;

    @GetMapping("/list")
    public ListLectureResponse listLecture(
            @RequestParam(name = "year") int year,
            @RequestParam(name = "courseId") int courseId,
            @RequestParam(name = "subjectId") int subjectId,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return ListLectureResponse.failed(ListLectureResponse.Error.invalidCredentials);
        }

        return listLectureService.list(year, courseId, subjectId);
    }

    @GetMapping("/getLecture")
    public GetLectureResponse getLecture(
            @RequestParam(name = "year") int year,
            @RequestParam(name = "courseId") int courseId,
            @RequestParam(name = "subjectId") int subjectId,
            @RequestParam(name = "lectureId") int lectureId,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return GetLectureResponse.failed(GetLectureResponse.Error.invalidCredentials);
        }

        return getLectureService.getLecture(year, courseId, subjectId, lectureId);
    }
}
