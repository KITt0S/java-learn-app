package com.k1ts.task;

import com.k1ts.task.dao.ListPassedTasksDaoService;
import com.k1ts.task.request.ListPassedTasksResponse;
import com.k1ts.user.Role;
import com.k1ts.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ListPassedTasksService {
    private final ListPassedTasksDaoService listPassedTasksDaoService;
    private final UserService userService;

    public ListPassedTasksResponse listPassedTasks(String principalUsername, String studentUsername, int courseId, int subjectId) {

        if (!userService.getById(principalUsername).getRole().equals(Role.Admin)) {
            return ListPassedTasksResponse.failed(ListPassedTasksResponse.Error.insufficientPrivileges);
        }

        if (!userService.getById(studentUsername).getRole().equals(Role.Student)) {
            return ListPassedTasksResponse.failed(ListPassedTasksResponse.Error.invalidStudentUsername);
        }

        Map<Integer, List<Integer>> result = listPassedTasksDaoService.listPassedTasks(studentUsername, courseId, subjectId);

        return ListPassedTasksResponse.success(result);
    }
}
