package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_9;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice9Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-9-task-5", TaskCheckService.class);

        addCorrectTestCase("""
                import java.util.function.*;
                
                public class Program {
                   
                       public int cube(int number, UnaryOperator<Integer> cubeOperator) {
                           return cubeOperator.apply(number);
                       }
               
                       public int cube(int number) {
               
                           return cube(number, integer -> integer * integer * integer);
                       }
                   }
                """);

        addIncorrectTestCase("""
                import java.util.function.*;
                
                public class Program {
                   
                       public int cube(int number, UnaryOperator<Integer> cubeOperator) {
                           return number * number * number;
                       }
               
                       public int cube(int number) {
               
                           return cube(number, integer -> integer * integer * integer);
                       }
                   }
                """);

        addIncorrectTestCase("""                
                import java.util.function.*;
                
                public class Program {
                   
                       public int cube(int number, UnaryOperator<Integer> cubeOperator) {
                           return cubeOperator.apply(number);
                       }
               
                       public int cube(int number) {
               
                           return number * number * number;
                       }
                   }
                """);
    }
}
