package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_8;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice8Task2CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-8-task-2", TaskCheckService.class);

        addCorrectTestCase("""
                interface Multiplier {
                         int multiply(int a, int b);
                     }
                 
                 public class Program {
             
                     public int multiplyNumbers(int a, int b) {
                         Multiplier multiplier = (first, second) -> first * second;
                        \s
                         return multiplier.multiply(a, b);
                     }
                 }
                """);

        addIncorrectTestCase("""
                interface Multiplier {
                         int multiply(int a, int b);
                     }
                 
                 public class Program {
                             
                     public int multiplyNumbers(int a, int b) {
                         Multiplier multiplier = (first, second) -> first;
                        \s
                         return multiplier.multiply(a, b);
                     }
                 }
                """);

        addIncorrectTestCase("""
                interface Multiplier {
                         int multiply(int a, int b);
                     }
                 
                 public class Program {
                             
                     public int multiplyNumbers(int a, int b) {
                         Multiplier multiplier = (first, second) -> second;
                        \s
                         return multiplier.multiply(a, b);
                     }
                 }
                """);
    }
}
