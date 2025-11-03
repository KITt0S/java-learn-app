package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_10;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice10Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-10-task-5", TaskCheckService.class);

        addCorrectTestCase("""
                public class Program {
                     \s
                      public String getVehicleType(String vehicle) {
                         \s
                          return switch (vehicle) {
                              case "Sedan", "Hatchback" -> "Car";
                              case "Lorry", "Pickup" -> "Truck";
                              case "Bike" -> "Bicycle";
                              case "Motorbike" -> "Motorcycle";
                              default -> "Unknown";
                          };
                      }
                  }
                """);

        addIncorrectTestCase("""
                public class Program {
                     \s
                      public String getVehicleType(String vehicle) {
                         \s
                          return switch (vehicle) {
                              case "Sedan", "Hatchback" -> "Car";
                              case "Lorry", "Pickup" -> "Car";
                              case "Bike" -> "Bicycle";
                              case "Motorbike" -> "Motorcycle";
                              default -> "Unknown";
                          };
                      }
                  }
                """);

        addIncorrectTestCase("""
                public class Program {
                     \s
                      public String getVehicleType(String vehicle) {
                         \s
                          return switch (vehicle) {
                              case "Sedan", "Hatchback" -> "Truck";
                              case "Lorry", "Pickup" -> "Truck";
                              case "Bike" -> "Bicycle";
                              case "Motorbike" -> "Motorcycle";
                              default -> "Unknown";
                          };
                      }
                  }
                """);
    }
}
