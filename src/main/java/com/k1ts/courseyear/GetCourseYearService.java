package com.k1ts.courseyear;

import com.k1ts.courseyear.request.GetCourseYearResponse;
import com.k1ts.user.Role;
import com.k1ts.user.UserService;
import com.k1ts.user.studentdata.StudentDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class GetCourseYearService {
    private final UserService userService;
    private final StudentDataService studentDataService;

    public GetCourseYearResponse getYear(String studentUsername, int courseNumber) {
        if (!userService.getById(studentUsername).getRole().equals(Role.Student)) {
            return GetCourseYearResponse.failed(GetCourseYearResponse.Error.insufficientPrivileges);
        }

        LocalDate firstCourseDate = studentDataService.getFirstCourseDate(studentUsername);

        return GetCourseYearResponse.success(firstCourseDate.getYear() + courseNumber - 1);
    }
}
