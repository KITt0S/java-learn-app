package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_10;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice10Task3CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-10-task-3", TaskCheckService.class);

        addCorrectTestCase("""
                public class Program {
                      \s
                       public String getSeason(String month) {
                          \s
                           return switch (month) {
                               case "December", "January", "February" -> "Winter";
                               case "March", "April", "May" -> "Spring";
                               case "June", "July", "August" -> "Summer";
                               case "September", "October", "November" -> "Autumn";
                               default -> "Invalid month";
                           };
                       }
                   }
                """);

        addIncorrectTestCase("""
                public class Program {
                      \s
                       public String getSeason(String month) {
                          \s
                           return switch (month) {
                               case "December", "January", "February" -> "Winter";
                               case "March", "April", "May" -> "Winter";
                               case "June", "July", "August" -> "Summer";
                               case "September", "October", "November" -> "Autumn";
                               default -> "Invalid month";
                           };
                       }
                   }
                """);

        addIncorrectTestCase("""
                public class Program {
                      \s
                       public String getSeason(String month) {
                          \s
                           return switch (month) {
                               case "December", "January", "February" -> "Spring";
                               case "March", "April", "May" -> "Spring";
                               case "June", "July", "August" -> "Summer";
                               case "September", "October", "November" -> "Autumn";
                               default -> "Invalid month";
                           };
                       }
                   }
                """);
    }
}
