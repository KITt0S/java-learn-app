package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_2;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice2Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-2-task-5", TaskCheckService.class);

        addCorrectTestCase("""                                
                enum TrafficLight {
                    RED, GREEN, YELLOW
                }
            
                public class TrafficLightClass {
            
                    public boolean isAllowedToMove(TrafficLight trafficLight) {
                        return trafficLight == TrafficLight.GREEN;
                    }
                }
                """);

        addIncorrectTestCase("""
                enum TrafficLight {
                    RED, GREEN
                }
            
                public class TrafficLightClass {
            
                    public boolean isAllowedToMove(TrafficLight trafficLight) {
                        return trafficLight == TrafficLight.GREEN;
                    }
                }
                """);

        addIncorrectTestCase("""
                enum TrafficLight {
                    RED, GREEN, YELLOW
                }
            
                public class TrafficLightClass {
            
                    public boolean isAllowedToMove(TrafficLight trafficLight) {
                        return trafficLight == TrafficLight.RED;
                    }
                }
                """);
    }
}
