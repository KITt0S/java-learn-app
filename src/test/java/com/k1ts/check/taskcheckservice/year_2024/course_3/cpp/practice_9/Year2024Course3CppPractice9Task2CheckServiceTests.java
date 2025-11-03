package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_9;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice9Task2CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-9-task-2", TaskCheckService.class);

        addCorrectTestCase("""
                import java.util.function.*;
                
                public class Program {
                    
                    public void greetUser(String name, Consumer<String> greeter) {
            
                        greeter.accept(name);
                    }
            
                    public void greetUser(String name, int number) {
            
                        greetUser(name, value -> {
                            for (int i = 0; i < number; i++) {
                                System.out.println("Привіт, " + value + "!");
                            }
                        });
                    }
                }
                """);

        addIncorrectTestCase("""
                import java.util.function.*;
                
                public class Program {
                    
                    public void greetUser(String name, Consumer<String> greeter) {
            
                        greeter.accept(name);
                    }
            
                    public void greetUser(String name, int number) {
            
                        greetUser(name, value -> {
                            for (int i = 0; i < number; i++) {
                                System.out.println(value);
                            }
                        });
                    }
                }
                """);

        addIncorrectTestCase("""              
                import java.util.function.*;
                
                public class Program {
                   
                       public void greetUser(String name, Consumer<String> greeter) {
               
                           greeter.accept(name);
                       }
               
                       public void greetUser(String name, int number) {
               
                           greetUser(name, value -> {
                               for (int i = 0; i < number - 1; i++) {
                                   System.out.println(value);
                               }
                           });
                       }
                   }
                """);
    }
}
