package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_5;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice5Task4CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-5-task-4", TaskCheckService.class);

        addCorrectTestCase("""
                import java.io.*;
                
                public class Program {
                  
                      public void countBytesInFile(String fileName) {
                          FileInputStream fis = null;
              
                          try {
                              fis = new FileInputStream(fileName);
                             \s
                              int bytesCount = 0;
                             \s
                              while (fis.read() != -1) {
                                  bytesCount++;
                              }
                             \s
                              System.out.println(bytesCount);
                          } catch (IOException e) {
                              e.printStackTrace();
                          } finally {
                              try {
                                  if (fis != null) {
                                      fis.close();
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
                  
                      public void countBytesInFile(String fileName) {
                          FileInputStream fis = null;
                              
                          try {
                              fis = new FileInputStream(fileName);
                             \s
                              int bytesCount = 0;
                             \s
                              while (fis.read() != -1) {
                                  bytesCount += 2;
                              }
                             \s
                              System.out.println(bytesCount);
                          } catch (IOException e) {
                              e.printStackTrace();
                          } finally {
                              try {
                                  if (fis != null) {
                                      fis.close();
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
                  
                      public void countBytesInFile(String fileName) {
                          FileInputStream fis = null;
                              
                          try {
                              fis = new FileInputStream(fileName);
                             \s
                              int bytesCount = 0;
                             \s
                              while (fis.read() != -1) {
                                  bytesCount++;
                              }
                             \s
                              System.out.println(bytesCount);
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                  }
                """);
    }
}
