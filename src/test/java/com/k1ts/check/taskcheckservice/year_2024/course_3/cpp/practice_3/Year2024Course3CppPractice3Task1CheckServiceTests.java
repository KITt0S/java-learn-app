package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_3;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice3Task1CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-3-task-1", TaskCheckService.class);

        addCorrectTestCase("""                                
                public class Program {
                    \s
                     public int product(Integer a, Integer b, Integer c) {
                         return a.intValue() * b.intValue() * c.intValue();
                     }
                 }
                """);

        addIncorrectTestCase("""
                public class Program {
                    \s
                     public int product(Integer a, Integer b, Integer c) {
                         return a * b * c;
                     }
                 }
                """);

        addIncorrectTestCase("""
                public class Program {
                    \s
                     public int product(Integer a, Integer b, Integer c) {
                         return a.intValue() * b.intValue() * c;
                     }
                 }
                """);
    }
}
