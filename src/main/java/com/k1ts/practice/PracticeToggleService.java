package com.k1ts.practice;

import com.k1ts.practice.request.practice_toggle.PracticeToggleRequest;
import com.k1ts.practice.request.practice_toggle.PracticeToggleResponse;
import com.k1ts.user.Role;
import com.k1ts.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PracticeToggleService {
    private final UserService userService;
    private final PracticePermissionService practicePermissionService;

    public PracticeToggleResponse togglePractices(PracticeToggleRequest request, String username) {
        if (!userService.getById(username).getRole().equals(Role.Admin)) {
            return PracticeToggleResponse.failed(PracticeToggleResponse.Error.insufficientPrivileges);
        }

        String studentUsername = request.getUsername();
        int year = request.getYear();
        int courseId = request.getCourseId();
        int subjectId = request.getSubjectId();
        boolean allowed = request.isAllowed();

        PracticePermissionCompositeId id = PracticePermissionCompositeId
                .builder()
                .user(userService.getById(studentUsername))
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .build();

        practicePermissionService.save(PracticePermission
                .builder()
                .id(id)
                .allowed(allowed)
                .build());

        return PracticeToggleResponse.success(allowed);
    }
}
