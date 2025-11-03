package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_8;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice8Task4CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-8-task-4", TaskCheckService.class);

        addCorrectTestCase("""
                interface Greeter {
                    void greet(String name);
                }
             
                public class Program {
                    
                    public void greetUser(String name) {
                        Greeter greeter = value -> System.out.println("Привіт, " + value + "!");
                       \s
                        greeter.greet(name);
                    }
                }
                """);

        addIncorrectTestCase("""
                interface Greeter {
                    void greet(String name);
                }
                             
                public class Program {
                    
                    public void greetUser(String name) {
                        Greeter greeter = value -> System.out.println("Привіт, " + value);
                       \s
                        greeter.greet(name);
                    }
                }
                """);

        addIncorrectTestCase("""
                interface Greeter {
                    void greet(String name);
                }
                             
                public class Program {
                    
                    public void greetUser(String name) {
                        Greeter greeter = value -> System.out.println("Привіт!");
                       \s
                        greeter.greet(name);
                    }
                }
                """);
    }
}
