package com.k1ts.taskresult;

import com.k1ts.taskresult.request.GetStudentTaskResultsResponse;
import com.k1ts.taskresult.request.GetStudentTaskResultsBySpecialityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class TaskResultController {
    private final GetStudentTaskResultsService getStudentTaskResultsService;
    private final GetStudentTaskResultsBySpecialityService getStudentTaskResultsBySpecialityService;

    @GetMapping("/getStudentTaskResults")
    public GetStudentTaskResultsResponse getStudentTaskResults(@RequestParam(name = "year") int year,
                                                        @RequestParam(name = "courseId") int courseId,
                                                        @RequestParam(name = "subjectId") int subjectId,
                                                        @RequestParam(name = "studentUsername") String studentUsername,
                                                        Principal principal) {

        if (principal ==null || principal.getName() == null) {
            return GetStudentTaskResultsResponse.failed(GetStudentTaskResultsResponse.Error.invalidCredentials);
        }

        return getStudentTaskResultsService.getStudentTaskResults(principal.getName(), year, courseId, subjectId, studentUsername);
    }

    @GetMapping("/getStudentTaskResultsBySpeciality")
    public GetStudentTaskResultsBySpecialityResponse getStudentTaskResultsBySpeciality(@RequestParam(name = "year") int year,
                                                                                       @RequestParam(name = "courseId") int courseId,
                                                                                       @RequestParam(name = "subjectId") int subjectId,
                                                                                       @RequestParam(name = "speciality") int speciality,
                                                                                       Principal principal) {

        if (principal == null || principal.getName() == null) {
            return GetStudentTaskResultsBySpecialityResponse.failed(GetStudentTaskResultsBySpecialityResponse.Error.invalidCredentials);
        }

        return getStudentTaskResultsBySpecialityService.getStudentTaskResultsBySpeciality(principal.getName(), year, courseId, subjectId, speciality);
    }
}
