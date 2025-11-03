package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_7;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice7Task1CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-7-task-1", TaskCheckService.class);

        addCorrectTestCase("""
                import java.util.*;
                
                public class Program {
                     \s
                      public void printListInReverseOrder(List<?> list) {
                           for (int i = list.size() - 1; i >= 0; i--) {
                               System.out.println(list.get(i));
                           }
                       }
                  }
                """);

        addIncorrectTestCase("""
                import java.util.*;
                
                public class Program {
                    \s
                     public void printListInReverseOrder(List<?> list) {
                          for (int i = 0; i < list.size() - 1; i++) {
                              System.out.println(list.get(i));
                          }
                      }
                 }
                """);

        addIncorrectTestCase("""
                import java.util.*;
                
                 public class Program {
                     \s
                      public void printListInReverseOrder(List<?> list) {
                           for (int i = list.size() - 1; i >= 0; i--) {
                               System.out.print(list.get(i));
                           }
                       }
                  }
                """);
    }
}
