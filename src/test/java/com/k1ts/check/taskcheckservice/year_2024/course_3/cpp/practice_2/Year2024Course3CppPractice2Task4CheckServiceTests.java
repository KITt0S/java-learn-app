package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_2;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice2Task4CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-2-task-4", TaskCheckService.class);

        addCorrectTestCase("""                                
                 enum Color {
                       RED, GREEN, BLUE, YELLOW
                   }
               
                   public class ColorClass {
               
                       public void printAllColors() {
                            String colors = "";
                            
                            for (int i = 0; i < Color.values().length; i++) {
                                if (i < Color.values().length - 1) {
                                    colors += Color.values()[i] + ", ";
                                } else {
                                    colors += Color.values()[i];
                                }
                            }
                
                            System.out.println(colors);
                        }
                   }
                """);

        addIncorrectTestCase("""
                enum Color {
                       RED, GREEN, BLUE, YELLOW
                   }
               
                   public class ColorClass {
               
                       public void printAllColors() {
                           for (Color color : Color.values()) {
                               System.out.print(color + " ");
                           }
                       }
                   }
                """);

        addIncorrectTestCase("""
                enum Color {
                       RED, GREEN, BLUE
                   }
               
                   public class ColorClass {
               
                       public void printAllColors() {
                           for (Color color : Color.values()) {
                               System.out.print(color + ", ");
                           }
                       }
                   }
                """);

        addIncorrectTestCase("""                                
                 enum Color {
                       RED, GREEN, BLUE, YELLOW
                   }
               
                   public class ColorClass {
               
                       public void printAllColors() {
                            System.out.println("RED, GREEN, BLUE, YELLOW");
                        }
                   }
                """);
    }
}
