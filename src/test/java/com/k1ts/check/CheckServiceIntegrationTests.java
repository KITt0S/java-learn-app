package com.k1ts.check;

import com.k1ts.check.request.check.CheckResponse;
import com.k1ts.dateprovider.CurrentDateProvider;
import com.k1ts.dateprovider.FixedDateProvider;
import com.k1ts.helper.UserHelperService;
import com.k1ts.taskresult.TaskResult;
import com.k1ts.taskresult.TaskResultCompositeId;
import com.k1ts.taskresult.TaskResultService;
import com.k1ts.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckServiceIntegrationTests {

    private static final LocalDate CURRENT_DATE = LocalDate.of(2024, Month.OCTOBER, 1);

    @Autowired
    private CheckService checkService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserHelperService userHelperService;

    @Autowired
    private TaskResultService taskResultService;

    @BeforeEach
    public void beforeEach() {
        taskResultService.deleteAll();
        checkService.setDateProvider(new FixedDateProvider(CURRENT_DATE));
    }

    @AfterEach
    public void afterEach() {
        checkService.setDateProvider(new CurrentDateProvider());
    }

    @DisplayName("Test that if student passes task before expiration date then his mark is 100")
    @Test
    public void testThatIfStudentPassesTaskBeforeExpirationDateThenHisMarkIs100() {
        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 1;
        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse response = checkService.check(studentUsername, year, courseId, subjectId, practiceId, taskId, code);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(CheckResponse.Error.ok, response.getError());
        Assertions.assertTrue(response.isTaskPassed());
        Assertions.assertEquals(100, response.getMark());

        TaskResult taskResult = taskResultService.getById(TaskResultCompositeId
                .builder()
                .user(userService.getById(studentUsername))
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .build());

        Assertions.assertEquals(1, taskResult.getAttemptCount());
        Assertions.assertEquals(100, taskResult.getMark());
        Assertions.assertEquals(CURRENT_DATE, taskResult.getPassedDate());
    }

    @DisplayName("Test that if student passes task before expiration date with second attempt then his mark is 99")
    @Test
    public void testThatIfStudentPassesTaskBeforeExpirationDateWithSecondAttemptThenHisMarkIs99() {
        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 1;
        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java це весело!");
                    }
                }
                """;

        checkService.check(studentUsername, year, courseId, subjectId, practiceId, taskId, code);

        code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse response = checkService.check(studentUsername, year, courseId, subjectId, practiceId, taskId, code);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(CheckResponse.Error.ok, response.getError());
        Assertions.assertTrue(response.isTaskPassed());
        Assertions.assertEquals(99, response.getMark());

        TaskResult taskResult = taskResultService.getById(TaskResultCompositeId
                .builder()
                .user(userService.getById(studentUsername))
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .build());

        Assertions.assertEquals(2, taskResult.getAttemptCount());
        Assertions.assertEquals(99, taskResult.getMark());
        Assertions.assertEquals(CURRENT_DATE, taskResult.getPassedDate());
    }

    @DisplayName("Test that if student passes task after expiration date then his mark is 60")
    @Test
    public void testThatIfStudentPassesTaskAfterExpirationDateThenHisMarkIs60() {
        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 1;
        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;
        checkService.setDateProvider(new FixedDateProvider(LocalDate.of(2024, Month.NOVEMBER, 26)));

        LocalDate[] expiredDates = {
                LocalDate.of(2024, Month.NOVEMBER, 25),
                LocalDate.of(2024, Month.NOVEMBER, 26)};

        for (LocalDate expiredDate : expiredDates) {
            checkService.setDateProvider(new FixedDateProvider(expiredDate));

            CheckResponse response = checkService.check(studentUsername, year, courseId, subjectId, practiceId, taskId, code);

            Assertions.assertTrue(response.isSuccess());
            Assertions.assertEquals(CheckResponse.Error.ok, response.getError());
            Assertions.assertTrue(response.isTaskPassed());
            Assertions.assertEquals(60, response.getMark());

            TaskResult taskResult = taskResultService.getById(TaskResultCompositeId
                    .builder()
                    .user(userService.getById(studentUsername))
                    .year(year)
                    .courseId(courseId)
                    .subjectId(subjectId)
                    .practiceId(practiceId)
                    .taskId(taskId)
                    .build());

            Assertions.assertEquals(1, taskResult.getAttemptCount());
            Assertions.assertEquals(60, taskResult.getMark());
            Assertions.assertEquals(expiredDate, taskResult.getPassedDate());

            taskResultService.deleteAll();
        }
    }

    @DisplayName("Test that if student passes task with second attempt after expiration date then his mark is 60")
    @Test
    public void testThatIfStudentPassesTaskWithSecondAttemptAfterExpirationDateThenHisMarkIs60() {
        checkService.setDateProvider(new FixedDateProvider(LocalDate.of(2024, Month.NOVEMBER, 25)));

        String studentUsername = userHelperService.generateStudent();

        int year = 2024;
        int courseId = 2;
        int subjectId = 2;
        int practiceId = 1;
        int taskId = 1;

        String code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java це весело!");
                    }
                }
                """;

        checkService.check(studentUsername, year, courseId, subjectId, practiceId, taskId, code);

        code = """
                public class JavaIsFunClass {
                    public static void main(String[] args) {
                        System.out.println("Java — це весело!");
                    }
                }
                """;

        CheckResponse response = checkService.check(studentUsername, year, courseId, subjectId, practiceId, taskId, code);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(CheckResponse.Error.ok, response.getError());
        Assertions.assertTrue(response.isTaskPassed());
        Assertions.assertEquals(60, response.getMark());

        TaskResult taskResult = taskResultService.getById(TaskResultCompositeId
                .builder()
                .user(userService.getById(studentUsername))
                .year(year)
                .courseId(courseId)
                .subjectId(subjectId)
                .practiceId(practiceId)
                .taskId(taskId)
                .build());

        Assertions.assertEquals(2, taskResult.getAttemptCount());
        Assertions.assertEquals(60, taskResult.getMark());
        Assertions.assertEquals(LocalDate.of(2024, Month.NOVEMBER, 25), taskResult.getPassedDate());
    }
}