package com.k1ts.subject;

import com.k1ts.subject.request.list.ListSubjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/subject")
@RestController
public class SubjectController {
    private final ListSubjectService listSubjectService;

    @GetMapping("/list")
    public ListSubjectResponse listSubjects(
            @RequestParam(name = "year") int year,
            @RequestParam(name = "courseId") int courseId,
            Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ListSubjectResponse.failed(ListSubjectResponse.Error.invalidCredentials);
        }

        return listSubjectService.listSubjects(principal.getName(), year, courseId);
    }
}
