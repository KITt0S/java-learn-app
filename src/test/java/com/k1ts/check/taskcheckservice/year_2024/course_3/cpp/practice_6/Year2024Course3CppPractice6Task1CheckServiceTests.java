package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_6;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice6Task1CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-6-task-1", TaskCheckService.class);

        addCorrectTestCase("""
                public class Box<T> {
                
                     private T value;
                    \s
                     public void set(T value) {
                         this.value = value;
                     }
                    \s
                     public T get() {
                         return value;
                     }
                 }
                """);

        addIncorrectTestCase("""
                 public class Box {
                                
                     private int value;
                    \s
                     public void set(int value) {
                         this.value = value;
                     }
                    \s
                     public int get() {
                         return value;
                     }
                 }
                """);

        addIncorrectTestCase("""
                 public class Box<T> {
                                
                     private T value;
                    \s
                     public void set(T value) {
                         this.value = value;
                     }
                 }
                """);
    }
}
