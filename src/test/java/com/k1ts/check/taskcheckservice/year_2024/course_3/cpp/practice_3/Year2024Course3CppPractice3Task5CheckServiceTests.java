package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_3;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice3Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-3-task-5", TaskCheckService.class);

        addCorrectTestCase("""                                
                public class Program {
                 
                     public Integer sum(Integer[] intArray) {
                         int result = 0;
             
                         for (Integer integer : intArray) {
                             result += integer.intValue();
                         }
                        
                         return Integer.valueOf(result);
                     }
                 }
                """);

        addIncorrectTestCase("""
                public class Program {
                 
                     public Integer sum(Integer[] intArray) {
                         int result = 0;
             
                         for (Integer integer : intArray) {
                             result += integer;
                         }
                        
                         return Integer.valueOf(result);
                     }
                 }
                """);

        addIncorrectTestCase("""
                public class Program {
                 
                     public Integer sum(Integer[] intArray) {
                         int result = 0;
             
                         for (Integer integer : intArray) {
                             result += integer.intValue();
                         }
                        
                         return result;
                     }
                 }
                """);
    }
}
