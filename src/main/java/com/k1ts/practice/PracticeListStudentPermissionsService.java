package com.k1ts.practice;

import com.k1ts.practice.dao.PracticeListStudentPermissionsDaoService;
import com.k1ts.practice.request.PracticeListStudentPermissionsResponse;
import com.k1ts.user.Role;
import com.k1ts.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PracticeListStudentPermissionsService {
    private final UserService userService;
    private final PracticeListStudentPermissionsDaoService practiceListStudentPermissionsDaoService;

    public PracticeListStudentPermissionsResponse getStudentPermissions(
            String username, int year, int courseId, int subjectId, int speciality) {

        if (!userService.getById(username).getRole().equals(Role.Admin)) {
            return PracticeListStudentPermissionsResponse.failed(PracticeListStudentPermissionsResponse.Error.insufficientPrivileges);
        }

        List<PracticeListStudentPermissionsResponse.StudentPracticePermission> studentPermissions =
                practiceListStudentPermissionsDaoService.getStudentPermissions(year, courseId, subjectId, speciality);

        return PracticeListStudentPermissionsResponse.success(studentPermissions);
    }
}
