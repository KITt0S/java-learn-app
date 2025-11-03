package com.k1ts.check;

import com.k1ts.check.request.check.CheckResponse;
import com.k1ts.check.request.javatask.JavaTaskCheckRequest;
import com.k1ts.check.request.javatask.JavaTaskCheckResponse;
import com.k1ts.dateprovider.CurrentDateProvider;
import com.k1ts.dateprovider.DateProvider;
import com.k1ts.practice.PracticePermissionService;
import com.k1ts.taskresult.TaskResult;
import com.k1ts.taskresult.TaskResultCompositeId;
import com.k1ts.taskresult.TaskResultService;
import com.k1ts.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class CheckService {
    private final UserService userService;
    private final TaskResultService taskResultService;
    private final PracticePermissionService practicePermissionService;

    private RestTemplate restTemplate;

    @Setter
    private DateProvider dateProvider;

    @Value("${javaTaskDomain}")
    private String javaTaskDomain;

    @Value("${javaTaskToken}")
    private String javaTaskApiToken;

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.dateProvider = new CurrentDateProvider();
    }

    public CheckResponse check(String username, int year, int courseId, int subjectId, int practiceId, int taskId, String code) {

        if (!practicePermissionService.isPracticesAllowed(username, year, courseId, subjectId)) {
            return CheckResponse.failed(CheckResponse.Error.practiceIsNotAllowed);
        }

        if (taskResultService.isTaskPassed(username, year, courseId, subjectId, practiceId, taskId)) {
            return CheckResponse.success(taskResultService
                    .getById(TaskResultCompositeId
                            .builder()
                            .user(userService.getById(username))
                            .year(year)
                            .courseId(courseId)
                            .subjectId(subjectId)
                            .practiceId(practiceId)
                            .taskId(taskId)
                            .build())
                    .getMark());
        }

        String url = javaTaskDomain + "/task/check";

        ResponseEntity<JavaTaskCheckResponse> responseEntity = restTemplate.postForEntity(
                url,
                JavaTaskCheckRequest
                        .builder()
                        .token(javaTaskApiToken)
                        .year(year)
                        .courseId(courseId)
                        .subjectId(subjectId)
                        .practiceId(practiceId)
                        .taskId(taskId)
                        .code(code)
                        .build(),
                JavaTaskCheckResponse.class);

        if (responseEntity.getStatusCode().value() != 200) {
            throw new IllegalStateException("Response entity returned not 200");
        }

        JavaTaskCheckResponse javaTaskCheckResponse = responseEntity.getBody();

        if (javaTaskCheckResponse == null) {
            throw new IllegalStateException("Response is null");
        }

        if (javaTaskCheckResponse.getError() != JavaTaskCheckResponse.Error.ok) {
            throw new IllegalStateException("Response error: " + javaTaskCheckResponse.getError().name());
        }

        CheckResponse response = CheckResponse
                .builder()
                .success(javaTaskCheckResponse.isSuccess())
                .taskPassed(javaTaskCheckResponse.isTaskPassed())
                .successResult(javaTaskCheckResponse.getSuccessResult())
                .failedResult(javaTaskCheckResponse.getFailedResult())
                .codeResult(javaTaskCheckResponse.getCodeResult())
                .errorResult(javaTaskCheckResponse.getErrorResult())
                .error(CheckResponse.Error.valueOf(javaTaskCheckResponse.getError().name()))
                .build();

        TaskResultCompositeId id = TaskResultCompositeId
                .builder()
                .user(userService.getById(username))
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .build();

        if (!taskResultService.exists(id) && !response.isTaskPassed()) {
            taskResultService.save(TaskResult
                    .builder()
                    .id(id)
                    .passed(false)
                    .attemptCount(1)
                    .build());

            return response;
        }

        if (!taskResultService.exists(id) && response.isTaskPassed()) {
            int mark = dateProvider
                    .getDate()
                    .isBefore(CheckConstants.YEAR_COURSE_SUBJECT_EXPIRED_DATE_MAP
                            .get(year)
                            .get(courseId)
                            .get(subjectId)) ? 100 : 60;

            taskResultService.save(TaskResult
                    .builder()
                    .id(id)
                    .code(code)
                    .passed(true)
                    .attemptCount(1)
                    .mark(mark)
                    .passedDate(dateProvider.getDate())
                    .build());

            response.setMark(mark);

            return response;
        }

        TaskResult taskResult = taskResultService.getById(id);

        if (taskResultService.exists(id) && !response.isTaskPassed()) {
            taskResultService.save(TaskResult
                    .builder()
                    .id(id)
                    .passed(false)
                    .attemptCount(taskResult.getAttemptCount() + 1)
                    .build());

            return response;
        }

        int mark = dateProvider
                .getDate()
                .isBefore(CheckConstants.YEAR_COURSE_SUBJECT_EXPIRED_DATE_MAP
                        .get(year)
                        .get(courseId)
                        .get(subjectId)) ? getMark(taskResult.getAttemptCount() + 1) : 60;

        taskResultService.save(TaskResult
                .builder()
                .id(id)
                .code(code)
                .passed(true)
                .attemptCount(taskResult.getAttemptCount() + 1)
                .mark(mark)
                .passedDate(dateProvider.getDate())
                .build());

        response.setMark(mark);

        return response;
    }

    private int getMark(int attemptCount) {
        // Якщо студент здав з першої спроби
        if (attemptCount == 1) {
            return 100;
        }

        // Якщо студент здав з 2-ї по 5-ту спробу, зниження на 1 бал за кожну спробу
        if (attemptCount >= 2 && attemptCount <= 5) {
            return 100 - (attemptCount - 1);
        }

        // Якщо студент здав з 6-ї по 22-гу спробу, зниження на 2 бали за кожну спробу
        if (attemptCount >= 6 && attemptCount <= 22) {
            return 100 - 5 - 2 * (attemptCount - 5);
        }

        // Якщо студент здав з 23-ї спроби, оцінка фіксована на 60
        return 60;
    }
}
