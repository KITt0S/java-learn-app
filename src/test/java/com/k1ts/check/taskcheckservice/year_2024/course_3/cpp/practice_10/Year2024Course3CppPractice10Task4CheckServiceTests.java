package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_10;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice10Task4CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-10-task-4", TaskCheckService.class);

        addCorrectTestCase("""
                public class Program {
                    \s
                     public String getAgeDiapason(String ageGroup) {
                        \s
                         return switch (ageGroup) {
                            \s
                             case "Child" -> "0-12";
                            \s
                             case "Teenager" -> "13-19";
                            \s
                             case "Adult" -> "20-64";
                            \s
                             case "Senior" -> "> 64";
                            \s
                             default -> "Unknown";
                         };
                     }
                 }
                """);

        addIncorrectTestCase("""
                public class Program {
                    \s
                     public String getAgeDiapason(String ageGroup) {
                        \s
                         return switch (ageGroup) {
                            \s
                             case "Child" -> "0-11";
                            \s
                             case "Teenager" -> "13-19";
                            \s
                             case "Adult" -> "20-64";
                            \s
                             case "Senior" -> "> 64";
                            \s
                             default -> "Unknown";
                         };
                     }
                 }
                """);

        addIncorrectTestCase("""
                public class Program {
                    \s
                     public String getAgeDiapason(String ageGroup) {
                        \s
                         return switch (ageGroup) {
                            \s
                             case "Child" -> "0-12";
                            \s
                             case "Teenager" -> "13-18";
                            \s
                             case "Adult" -> "20-64";
                            \s
                             case "Senior" -> "> 64";
                            \s
                             default -> "Unknown";
                         };
                     }
                 }
                """);
    }
}
