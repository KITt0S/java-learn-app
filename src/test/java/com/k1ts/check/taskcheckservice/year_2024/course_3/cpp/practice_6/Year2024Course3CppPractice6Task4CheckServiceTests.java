package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_6;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice6Task4CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-6-task-4", TaskCheckService.class);

        addCorrectTestCase("""
                public class Container<T extends Number> {
                     
                     public void sum(T[] numbers) {
                         double total = 0;
             
                         for (T number : numbers) {
                             total += number.doubleValue();
                         }
             
                         System.out.println(total);
                     }
                 }
                """);

        addIncorrectTestCase("""
                public class Container<T extends Number> {
                     
                     public void sum(T[] numbers) {
                         double total = 0;
             
                         for (T number : numbers) {
                             total += 1;
                         }
             
                         System.out.println(total);
                     }
                 }
                """);

        addIncorrectTestCase("""
                public class Container<T extends Number> {
                     
                     public void sum(T[] numbers) {
                         double total = 0;
             
                         for (T number : numbers) {
                             total += number.doubleValue();
                         }
                     }
                 }
                """);
    }
}
