package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_8;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice8Task1CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-8-task-1", TaskCheckService.class);

        addCorrectTestCase("""
                interface Printer {
                   void print(String message);
                }
               
                public class Program {
               
                    public void printMessage(String message) {
                        Printer printer = text -> System.out.println(text);
               
                        printer.print(message);
                   }
                }
                """);

        addIncorrectTestCase("""
                interface Printer {
                   void print(String message);
                }
               
                public class Program {
               
                    public void printMessage(String message) {
                        Printer printer = text -> System.out.println(text + " " + text);
               
                        printer.print(message);
                   }
                }
                """);

        addIncorrectTestCase("""
                private interface Printer {
                   void print(String message);
                }
               
                public class Program {
               
                    public void printMessage(String message) {
                        Printer printer = text -> System.out.println();
               
                        printer.print(message);
                   }
                }
                """);
    }
}
