package com.k1ts.practice;

import com.k1ts.practice.request.PracticeListStudentPermissionsResponse;
import com.k1ts.practice.request.practice_toggle.PracticeToggleRequest;
import com.k1ts.practice.request.practice_toggle.PracticeToggleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/practice")
@RestController
public class PracticeController {

    private final PracticeToggleService practiceToggleService;
    private final PracticeListStudentPermissionsService practiceListStudentPermissionsService;

    @PostMapping("/toggle")
    PracticeToggleResponse toggleTask(@RequestBody PracticeToggleRequest request, Principal principal) {

        if (principal == null || principal.getName() == null) {
            return PracticeToggleResponse.failed(PracticeToggleResponse.Error.invalidCredentials);
        }

        return practiceToggleService.togglePractices(request, principal.getName());
    }

    @GetMapping("/getStudentPracticePermissions")
    PracticeListStudentPermissionsResponse getStudentPracticePermissions(@RequestParam(name = "year") int year,
                                                                         @RequestParam(name = "courseId") int courseId,
                                                                         @RequestParam(name = "subjectId") int subjectId,
                                                                         @RequestParam(name = "speciality") int speciality,
                                                                         Principal principal) {

        if (principal == null || principal.getName() == null) {
            return PracticeListStudentPermissionsResponse.failed(PracticeListStudentPermissionsResponse.Error.invalidCredentials);
        }

        return practiceListStudentPermissionsService.getStudentPermissions(principal.getName(), year, courseId, subjectId, speciality);
    }
}
