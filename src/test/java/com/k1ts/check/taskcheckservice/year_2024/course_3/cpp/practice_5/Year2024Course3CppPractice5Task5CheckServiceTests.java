package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_5;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice5Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-5-task-5", TaskCheckService.class);

        addCorrectTestCase("""
                import java.io.*;
                
                public class Program {
                   
                       public void appendToFile(String fileName, String text) {
                           FileOutputStream fos = null;
               
                           try {
                               fos = new FileOutputStream(fileName, true);
                               fos.write(text.getBytes());
                           } catch (IOException e) {
                               e.printStackTrace();
                           } finally {
                               try {
                                   if (fos != null) {
                                       fos.close();
                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                   }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                                
                public class Program {
                   
                       public void appendToFile(String fileName, String text) {
                           FileOutputStream fos = null;
               
                           try {
                               fos = new FileOutputStream(fileName);
                               fos.write(text.getBytes());
                           } catch (IOException e) {
                               e.printStackTrace();
                           } finally {
                               try {
                                   if (fos != null) {
                                       fos.close();
                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                   }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                                
                public class Program {
                   
                       public void appendToFile(String fileName, String text) {
                           FileOutputStream fos = null;
               
                           try {
                               fos = new FileOutputStream(fileName, true);
                           } catch (IOException e) {
                               e.printStackTrace();
                           } finally {
                               try {
                                   if (fos != null) {
                                       fos.close();
                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                   }
                """);
    }
}
