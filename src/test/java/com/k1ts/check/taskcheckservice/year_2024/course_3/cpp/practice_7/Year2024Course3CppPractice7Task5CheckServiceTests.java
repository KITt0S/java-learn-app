package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_7;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice7Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-7-task-5", TaskCheckService.class);

        addCorrectTestCase("""
                import java.util.*;
                
                public class Program {
                  
                      public Number getAverage(List<? extends Number> numbers) {
                          return numbers.stream().mapToDouble(Number::doubleValue).average().orElse(0);
                      }
                  }
                """);

        addIncorrectTestCase("""
                import java.util.*;
                
                public class Program {
                  
                      public Number getAverage(List<? extends Number> numbers) {
                          return 0;
                      }
                  }
                """);

        addIncorrectTestCase("""
                import java.util.*;
                
                public class Program {
                  
                      public Number getAverage(List<? extends Number> numbers) {
                          return numbers.stream().mapToDouble(Number::doubleValue).max().orElse(0);
                      }
                  }
                """);
    }
}
