package com.k1ts.task;

import com.k1ts.task.request.GetTaskResponse;
import com.k1ts.task.request.ListPassedTasksResponse;
import com.k1ts.task.request.ListPracticesResponse;
import com.k1ts.task.request.ListTasksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class TaskController {
    private final ListPracticesService listPracticesService;
    private final ListTasksService listTasksService;
    private final GetTaskService getTaskService;
    private final ListPassedTasksService listPassedTasksService;

    @GetMapping("/listPractices")
    public ListPracticesResponse listPractices(@RequestParam(name = "year") int year,
                                               @RequestParam(name = "courseId") int courseId,
                                               @RequestParam(name = "subjectId") int subjectId,
                                               Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ListPracticesResponse.failed(ListPracticesResponse.Error.invalidCredentials);
        }

        return listPracticesService.listPractices(year, courseId, subjectId);
    }

    @GetMapping("/listTasks")
    public ListTasksResponse listTasks(@RequestParam(name = "year") int year,
                                       @RequestParam(name = "courseId") int courseId,
                                       @RequestParam(name = "subjectId") int subjectId,
                                       @RequestParam(name = "practiceId") int practiceId,
                                       Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ListTasksResponse.failed(ListTasksResponse.Error.invalidCredentials);
        }

        return listTasksService.listTasks(year, courseId, subjectId, practiceId);
    }

    @GetMapping("/getTask")
    public GetTaskResponse getTask(@RequestParam(name = "year") int year,
                                   @RequestParam(name = "courseId") int courseId,
                                   @RequestParam(name = "subjectId") int subjectId,
                                   @RequestParam(name = "practiceId") int practiceId,
                                   @RequestParam(name = "taskId") int taskId,
                                   Principal principal) {
        if (principal == null || principal.getName() == null) {
            return GetTaskResponse.failed(GetTaskResponse.Error.invalidCredentials);
        }

        return getTaskService.getTask(
                principal.getName(),
                GetTaskService.TaskParams
                        .builder()
                        .year(year)
                        .courseId(courseId)
                        .subjectId(subjectId)
                        .practiceId(practiceId)
                        .taskId(taskId)
                        .build());
    }

    @GetMapping("/listPassedTasks")
    ListPassedTasksResponse listPassedTasks(
            @RequestParam(name = "studentUsername") String studentUsername,
            @RequestParam(name = "courseId") int courseId,
            @RequestParam(name = "subjectId") int subjectId,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return ListPassedTasksResponse.failed(ListPassedTasksResponse.Error.invalidCredentials);
        }

        return listPassedTasksService.listPassedTasks(principal.getName(), studentUsername, courseId, subjectId);
    }
}
