package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_10;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice10Task2CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-10-task-2", TaskCheckService.class);

        addCorrectTestCase("""
                public class Program {
                     \s
                      public String getGradeComment(char grade) {
                         \s
                          return switch (grade) {
                              case 'A' -> "Excellent";
                              case 'B' -> "Good";
                              case 'C' -> "Average";
                              case 'D' -> "Poor";
                              case 'F' -> "Fail";
                              default -> "Invalid grade";
                          };
                      }
                  }
                """);

        addIncorrectTestCase("""
                public class Program {
                     \s
                      public String getGradeComment(char grade) {
                         \s
                          return switch (grade) {
                              case 'A' -> "Excellent";
                              case 'B' -> "Excellent";
                              case 'C' -> "Average";
                              case 'D' -> "Poor";
                              case 'F' -> "Fail";
                              default -> "Invalid grade";
                          };
                      }
                  }
                """);

        addIncorrectTestCase("""
                public class Program {
                     \s
                      public String getGradeComment(char grade) {
                         \s
                          return switch (grade) {
                              case 'A' -> "Excellent";
                              case 'B' -> "Good";
                              case 'C' -> "Good";
                              case 'D' -> "Poor";
                              case 'F' -> "Fail";
                              default -> "Invalid grade";
                          };
                      }
                  }
                """);
    }
}
