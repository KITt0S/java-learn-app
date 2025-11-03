package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_8;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice8Task5CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-8-task-5", TaskCheckService.class);

        addCorrectTestCase("""
                interface Power {
                    int power(int base, int exponent);
                }
                 
                public class Program {
             
                    public int calculatePower(int base, int exponent) {
                        Power power = (baseValue, exponentValue) -> (int) Math.pow(baseValue, exponentValue);
            
                        return power.power(base, exponent);
                    }
                }
                """);

        addIncorrectTestCase("""
                interface Power {
                    int power(int base, int exponent);
                }
                 
                public class Program {
             
                    public int calculatePower(int base, int exponent) {
                        Power power = (baseValue, exponentValue) -> (int) baseValue * exponentValue;
            
                        return power.power(base, exponent);
                    }
                }
                """);

        addIncorrectTestCase("""
                interface Power {
                    int power(int base, int exponent);
                }
                 
                public class Program {
             
                    public int calculatePower(int base, int exponent) {
                        Power power = (baseValue, exponentValue) -> baseValue;
            
                        return power.power(base, exponent);
                    }
                }
                """);
    }
}
