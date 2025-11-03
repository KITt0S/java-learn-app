package com.k1ts.subject;

import com.k1ts.subject.request.list.ListSubjectResponse;
import com.k1ts.user.Role;
import com.k1ts.user.UserService;
import com.k1ts.user.studentdata.StudentDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ListSubjectService {
    private final UserService userService;
    private final StudentDataService studentDataService;

    @SuppressWarnings("all")
    public ListSubjectResponse listSubjects(String username, int year, int courseId) {
        if (userService.getById(username).getRole() != Role.Student) {
            return ListSubjectResponse.failed(ListSubjectResponse.Error.insufficientPrivileges);
        }

        Map<Integer, String> result = new HashMap<>();

        int speciality = studentDataService.getSpeciality(username);

        return switch (year) {

            case 2024 -> switch (courseId) {

                case 2 -> switch (speciality) {

                    case 14, 121, 122 -> {
                        result.put(1, "Java-програмування (2 семестр)");
                        result.put(2, "Об'єктно-орієнтоване програмування (1 семестр)");
                        result.put(3, "Об'єктно-орієнтоване програмування (2 семестр)");

                        yield ListSubjectResponse.success(result);
                    }

                    case 123, 125, 172 -> {
                        result.put(1, "Java-програмування (2 семестр)");

                        yield ListSubjectResponse.success(result);
                    }

                    default -> ListSubjectResponse.failed(ListSubjectResponse.Error.invalidSpeciality);
                };

                case 3 -> {
                    result.put(1, "Крос-платформне програмування");
                    yield ListSubjectResponse.success(result);
                }

                case 4 -> {
                    result.put(1, "Java-програмування");
                    yield ListSubjectResponse.success(result);
                }

                default -> ListSubjectResponse.failed(ListSubjectResponse.Error.invalidCourse);
            };

            default -> ListSubjectResponse.failed(ListSubjectResponse.Error.invalidYear);
        };
    }
}
