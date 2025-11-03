package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_2;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice2Task2CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-2-task-2", TaskCheckService.class);

        addCorrectTestCase("""                                
                enum Season {
                     WINTER(-20), SPRING(10), SUMMER(30), FALL(5);
             
                     private final int averageTemperature;
             
                     Season(int averageTemperature) {
                         this.averageTemperature = averageTemperature;
                     }
             
                     public int getAverageTemperature() {
                         return averageTemperature;
                     }
                 }
             
                 public class SeasonClass {
             
                     public void printSeasonInfo(Season season) {
                         System.out.println(season.name() + ": " + season.getAverageTemperature());
                     }
                 }
                """);

        addIncorrectTestCase("""
                enum Season {
                     WINTER(-20), SPRING(10), SUMMER(30), FALL(5);
             
                     private final int averageTemperature;
             
                     Season(int averageTemperature) {
                         this.averageTemperature = averageTemperature;
                     }
             
                     public int getAverageTemperature() {
                         return averageTemperature;
                     }
                 }
             
                 public class SeasonClass {
             
                     public void printSeasonInfo(Season season) {
                         System.out.println(season.name());
                     }
                 }
                """);

        addIncorrectTestCase("""
                enum Season {
                     WINTER(-20), SPRING(10), SUMMER(30), FALL(5);
             
                     private final int averageTemperature;
             
                     Season(int averageTemperature) {
                         this.averageTemperature = averageTemperature;
                     }
             
                     public int getAverageTemperature() {
                         return averageTemperature;
                     }
                 }
             
                 public class SeasonClass {
             
                     public void printSeasonInfo(Season season) {
                         System.out.println(season.getAverageTemperature());
                     }
                 }
                """);
    }
}
