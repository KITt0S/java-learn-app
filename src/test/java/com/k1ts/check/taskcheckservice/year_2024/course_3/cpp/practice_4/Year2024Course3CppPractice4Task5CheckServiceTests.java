package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_4;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice4Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-4-task-5", TaskCheckService.class);

        addCorrectTestCase("""
                import java.io.*;
                                           
                public class Program {
                  
                      public void countWords() {
                          BufferedReader br = null;
              
                          try {
                              br = new BufferedReader(new InputStreamReader(System.in));
                              String text = br.readLine();
              
                              System.out.println(text.split(" ").length);
                          } catch (IOException e) {
                              e.printStackTrace();
                          } finally {
                              if (br != null) {
                                  try {
                                      br.close();
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }
                              }
                          }
                      }
                  }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                                
                public class Program {
                  
                      public void countWords() {
                          BufferedReader br = null;
              
                          try {
                              br = new BufferedReader(new InputStreamReader(System.in));
                              String text = br.readLine();
              
                              System.out.println(text.split(",").length);
                          } catch (IOException e) {
                              e.printStackTrace();
                          } finally {
                              if (br != null) {
                                  try {
                                      br.close();
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }
                              }
                          }
                      }
                  }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                                
                public class Program {
                  
                      public void countWords() {
                          BufferedReader br = null;
              
                          try {
                              br = new BufferedReader(new InputStreamReader(System.in));
                              String text = br.readLine();
              
                              System.out.println(text.split(" ").length);
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                  }
                """);
    }
}
