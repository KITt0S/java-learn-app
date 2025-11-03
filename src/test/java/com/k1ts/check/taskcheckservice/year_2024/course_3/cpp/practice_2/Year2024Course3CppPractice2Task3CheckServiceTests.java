package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_2;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice2Task3CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-2-task-3", TaskCheckService.class);

        addCorrectTestCase("""                                
                enum Month {
                      JANUARY(31),
                      FEBRUARY(28),
                      MARCH(31),\s
                      APRIL(30),
                      MAY(31),
                      JUNE(30),
                      JULY(31),\s
                      AUGUST(31),\s
                      SEPTEMBER(30),\s
                      OCTOBER(31),\s
                      NOVEMBER(30),\s
                      DECEMBER(31);
              
                      private final int dayCount;
              
                      Month(int dayCount) {
                          this.dayCount = dayCount;
                      }
                     \s
                      public int getDayCount() {
                          return dayCount;
                      }
                  }
              
                  public class MonthClass {
              
                      public void printDayCount(Month month) {
                          System.out.println(month.name() + ": " + month.getDayCount());
                      }
                  }
                """);

        addIncorrectTestCase("""
                enum Month {
                      JANUARY(31),
                      FEBRUARY(28),
                      MARCH(31),\s
                      APRIL(30),
                      MAY(31),
                      JUNE(30),
                      JULY(31),\s
                      AUGUST(31),\s
                      SEPTEMBER(30),\s
                      OCTOBER(31),\s
                      NOVEMBER(30),\s
                      DECEMBER(31);
                              
                      private final int dayCount;
                              
                      Month(int dayCount) {
                          this.dayCount = dayCount;
                      }
                     \s
                      public int getDayCount() {
                          return dayCount;
                      }
                  }
                              
                  public class MonthClass {
                              
                      public void printDayCount(Month month) {
                          System.out.println(month.name());
                      }
                  }
                """);

        addIncorrectTestCase("""
                enum Month {
                      JANUARY(31),
                      FEBRUARY(28),
                      MARCH(31),\s
                      APRIL(30),
                      MAY(31),
                      JUNE(30),
                      JULY(31),\s
                      AUGUST(31),\s
                      SEPTEMBER(30),\s
                      OCTOBER(31),\s
                      NOVEMBER(30),\s
                      DECEMBER(31);
                              
                      private final int dayCount;
                              
                      Month(int dayCount) {
                          this.dayCount = dayCount;
                      }
                     \s
                      public int getDayCount() {
                          return dayCount;
                      }
                  }
                              
                  public class MonthClass {
                              
                      public void printDayCount(Month month) {
                          System.out.println(month.getDayCount());
                      }
                  }
                """);
    }
}
