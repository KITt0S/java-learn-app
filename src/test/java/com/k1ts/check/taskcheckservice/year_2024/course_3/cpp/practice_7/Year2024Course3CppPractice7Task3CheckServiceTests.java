package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_7;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice7Task3CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-7-task-3", TaskCheckService.class);

        addCorrectTestCase("""
                import java.util.*;
                
                public class Program {
            
                    public void addToList(List<? super Integer> list) {
                        for (int i = 0; i < 10; i++) {
                            list.add((i + 1) * 10);
                        }
                    }
                }
                """);

        addIncorrectTestCase("""
                import java.util.*;
                
                public class Program {
            
                    public void addToList(List<? super Integer> list) {
                        for (int i = 0; i < 10; i++) {
                            list.add((i + 1) * 5);
                        }
                    }
                }
                """);

        addIncorrectTestCase("""
                import java.util.*;
                
                public class Program {
            
                    public void addToList(List<? super Integer> list) {
                        for (int i = 0; i < 5; i++) {
                            list.add((i + 1) * 10);
                        }
                    }
                }
                """);
    }
}
