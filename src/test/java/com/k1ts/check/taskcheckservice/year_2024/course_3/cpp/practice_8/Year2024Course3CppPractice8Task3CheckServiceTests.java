package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_8;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice8Task3CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-8-task-3", TaskCheckService.class);

        addCorrectTestCase("""
                interface Checker {
                    boolean check(int number);
                }
                   
                public class Program {
            
                    public boolean isEven(int number) {
                        Checker checker = value -> value % 2 == 0;
            
                        return checker.check(number);
                    }
                }
                """);

        addIncorrectTestCase("""
                interface Checker {
                    boolean check(int number);
                }
                   
                public class Program {
            
                    public boolean isEven(int number) {
                        Checker checker = value -> value % 2 == 1;
            
                        return checker.check(number);
                    }
                }
                """);

        addIncorrectTestCase("""
                interface Checker {
                    boolean check(int number);
                }
                   
                public class Program {
            
                    public boolean isEven(int number) {
                        Checker checker = value -> true;
            
                        return checker.check(number);
                    }
                }
                """);
    }
}
