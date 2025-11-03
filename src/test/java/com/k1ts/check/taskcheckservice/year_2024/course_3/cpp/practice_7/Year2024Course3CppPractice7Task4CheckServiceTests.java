package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_7;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice7Task4CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-7-task-4", TaskCheckService.class);

        addCorrectTestCase("""
                import java.util.*;
                
                public class Program {
                 
                     public Number getUpperBound(List<? extends Number> list) {
                         List<? extends Number> sortedList = new ArrayList<>(list);
             
                         sortedList.sort((Comparator<Number>) (o1, o2) -> (int) (o2.doubleValue() - o1.doubleValue()));
             
                         return sortedList.get(0);
                     }
                 }
                """);

        addIncorrectTestCase("""
                import java.util.*;
                
                public class Program {
                 
                     public Number getUpperBound(List<? extends Number> list) {
                         List<? extends Number> sortedList = new ArrayList<>(list);
             
                         sortedList.sort((Comparator<Number>) (o1, o2) -> (int) (o1.doubleValue() - o2.doubleValue()));
             
                         return sortedList.get(0);
                     }
                 }
                """);

        addIncorrectTestCase("""
                import java.util.*;
                
                public class Program {
                 
                     public Number getUpperBound(List<? extends Number> list) {
                         return list.get(0);
                     }
                 }
                """);
    }
}
