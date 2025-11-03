package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_3;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice3Task3CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-3-task-3", TaskCheckService.class);

        addCorrectTestCase("""                                
                public class Program {
                   
                       public Integer sum(int a, int b, int c) {
                           return Integer.valueOf((a + b) + (b - c));
                       }
                   }
                """);

        addIncorrectTestCase("""
                public class Program {
                   
                       public Integer sum(int a, int b, int c) {
                           return (a + b) + (b - c);
                       }
                   }
                """);

        addIncorrectTestCase("""
                public class Program {
                  
                      public double divide(Double a, Double b, Double c) {
                          return a + b;
                      }
                  }
                """);
    }
}
