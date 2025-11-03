package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_6;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice6Task2CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-6-task-2", TaskCheckService.class);

        addCorrectTestCase("""
                public class Pair<K, V> {
                      private K key;
                      private V value;
                     \s
                      public void set(K key, V value) {
                          this.key = key;
                          this.value = value;
                      }
                     \s
                      public K getKey() {
                          return key;
                      }
                     \s
                      public V getValue() {
                          return value;
                      }
                  }
                """);

        addIncorrectTestCase("""
                 private static class Pair<K, V> {
                          private K key;
                          private V value;
                         \s
                          public void set(K key, V value) {
                              this.key = key;
                              this.value = value;
                          }
                         \s
                          public K getKey() {
                              return key;
                          }
                      }
                """);

        addIncorrectTestCase("""
                 private static class Pair<K, V> {
                          private K key;
                          private V value;
                         \s
                          public void set(K key, V value) {
                              this.key = key;
                              this.value = value;
                          }
                         \s
                          public V getValue() {
                              return value;
                          }
                      }
                """);
    }
}
