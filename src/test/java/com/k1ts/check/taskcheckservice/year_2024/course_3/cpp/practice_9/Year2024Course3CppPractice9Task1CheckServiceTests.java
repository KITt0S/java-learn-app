package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_9;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice9Task1CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-9-task-1", TaskCheckService.class);

        addCorrectTestCase("""
                import java.util.function.*;
                
                public class Program {
                  
                      public boolean isFactor(int number, Predicate<Integer> factorPredicate) {
              
                          return factorPredicate.test(number);
                      }
              
                      public boolean isFactorOf3(int number) {
                          return isFactor(number, n -> n % 3 == 0);
                      }
                  }
                """);

        addIncorrectTestCase("""
                import java.util.function.*;
                
                public class Program {
                  
                      public boolean isFactor(int number, Predicate<Integer> factorPredicate) {
              
                          return factorPredicate.test(number);
                      }
              
                      public boolean isFactorOf3(int number) {
                          return isFactor(number, n -> n % 2 == 0);
                      }
                  }
                """);

        addIncorrectTestCase("""
                import java.util.function.*;
                
                public class Program {
                  
                      public boolean isFactor(int number, Predicate<Integer> factorPredicate) {
              
                          return factorPredicate.test(number);
                      }
              
                      public boolean isFactorOf3(int number) {
                          return isFactor(number, n -> true);
                      }
                  }
                """);
    }
}
