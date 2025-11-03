package com.k1ts.courseyear;

import com.k1ts.courseyear.request.GetCourseYearResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class CourseYearController {
    private final GetCourseYearService getCourseYearService;

    @GetMapping("/getYearOfCourse")
    public GetCourseYearResponse getCourseYear(@RequestParam(name = "courseId") int courseId, Principal principal) {

        if (principal == null || principal.getName() == null) {
            return GetCourseYearResponse.failed(GetCourseYearResponse.Error.invalidCredentials);
        }

        return getCourseYearService.getYear(principal.getName(), courseId);
    }
}
