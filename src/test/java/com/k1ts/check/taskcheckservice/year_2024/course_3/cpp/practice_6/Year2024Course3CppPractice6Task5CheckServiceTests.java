package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_6;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice6Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-6-task-5", TaskCheckService.class);

        addCorrectTestCase("""
                public class Program {
                  
                      public <T extends Number> double findMax(T a, T b) {
                          return Math.max(a.doubleValue(), b.doubleValue());
                      }
                  }
                """);

        addIncorrectTestCase("""
                public class Program {
                  
                      public <T extends Number> double findMax(T a, T b) {
                          return Math.min(a.doubleValue(), b.doubleValue());
                      }
                  }
                """);

        addIncorrectTestCase("""
                public class Program {
                  
                      public <T extends Number> double findMax(T a, T b) {
                          return a.doubleValue() - b.doubleValue();
                      }
                  }
                """);
    }
}
