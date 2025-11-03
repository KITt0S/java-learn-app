package com.k1ts.check;

import com.k1ts.check.request.check.CheckRequest;
import com.k1ts.check.request.check.CheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class CheckController {

    private final CheckService checkService;

    @PostMapping("/check")
    public CheckResponse check(@RequestBody CheckRequest request, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return CheckResponse.failed(CheckResponse.Error.invalidCredentials);
        }

        return checkService.check(
                principal.getName(),
                request.getYear(),
                request.getCourseId(),
                request.getSubjectId(),
                request.getPracticeId(),
                request.getTaskId(),
                request.getCode());
    }
}
