package com.k1ts.task;

import com.k1ts.subject.SubjectIdDefiner;
import com.k1ts.task.request.GetTaskResponse;
import com.k1ts.taskresult.TaskResultService;
import io.sentry.Sentry;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class GetTaskService {
    private final TaskValidationService taskValidationService;
    private final SubjectIdDefiner subjectIdDefiner;
    private final TaskResultService taskResultService;

    public GetTaskResponse getTask(String username, TaskParams params) {

        if (taskValidationService.isInvalidCourseId(params.getCourseId())) {
            return GetTaskResponse.failed(GetTaskResponse.Error.invalidCourseId);
        }

        if (taskValidationService.isInvalidSubjectId(params.getYear(), params.getCourseId(), params.getSubjectId())) {
            return GetTaskResponse.failed(GetTaskResponse.Error.invalidSubjectId);
        }

        if (taskValidationService.isInvalidPracticeId(params.getYear(), params.getCourseId(), params.getSubjectId(), params.getPracticeId())) {
            return GetTaskResponse.failed(GetTaskResponse.Error.invalidPracticeId);
        }

        if (taskValidationService.isInvalidTaskId(params.getYear(), params.getCourseId(), params.getSubjectId(), params.getPracticeId(), params.getTaskId())) {
            return GetTaskResponse.failed(GetTaskResponse.Error.invalidTaskId);
        }

        return createTask(username, params);
    }

    private GetTaskResponse createTask(String username, TaskParams taskParams) {

        String fileName = "materials/" + taskParams.getYear() + "/" + taskParams.getCourseId() + "_course/" +
                subjectIdDefiner.getSubjectIdAsText(taskParams.getCourseId(), taskParams.getSubjectId()) +
                "/practices/" + "practice_" + taskParams.getPracticeId() + "/task_" + taskParams.getTaskId() + ".md";


        StringBuilder taskText = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String taskLine;

            while ((taskLine = reader.readLine()) != null) {
                taskText.append(taskLine);
                taskText.append("\n");
            }
        } catch (IOException e) {
            Sentry.captureException(e);
            throw new RuntimeException(e);
        }

        String code = null;

        if (taskResultService.isTaskPassed(
                username,
                taskParams.getYear(),
                taskParams.getCourseId(),
                taskParams.getSubjectId(),
                taskParams.getPracticeId(),
                taskParams.getTaskId())) {

            code = taskResultService.getCode(username,
                    taskParams.getYear(),
                    taskParams.getCourseId(),
                    taskParams.getSubjectId(),
                    taskParams.getPracticeId(),
                    taskParams.getTaskId());
        }

        return GetTaskResponse
                .builder()
                .success(true)
                .error(GetTaskResponse.Error.ok)
                .code(code)
                .taskText(taskText.toString().strip())
                .build();
    }

    @Getter
    @Builder
    public static class TaskParams {
        private final int year;
        private final int courseId;
        private final int subjectId;
        private final int practiceId;
        private final int taskId;
    }
}
