package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_6;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice6Task3CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-6-task-3", TaskCheckService.class);

        addCorrectTestCase("""
                public class Program {
                      \s
                       public <T> void printArray(T[] array) {
                           for (T entry : array) {
                               System.out.println(entry);
                           }
                       }
                   }
                """);

        addIncorrectTestCase("""
                 public class Program {
                      \s
                       public <T> void printArray(T[] array) {
                           for (T entry : array) {
                               System.out.print(entry);
                           }
                       }
                   }
                """);

        addIncorrectTestCase("""
                 public class Program {
                      \s
                       public <T> void printArray(T[] array) {                           
                       }
                   }
                """);
    }
}
