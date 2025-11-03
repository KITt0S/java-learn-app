package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_2;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice2Task1CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    public ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-2-task-1", TaskCheckService.class);

        addCorrectTestCase("""                                
                enum DayOfWeek {
                    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
                }
                
                public class HolidayClass {
                   
                    public boolean isHoliday(DayOfWeek day) {
                        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
                    }
                }
                """);

        addIncorrectTestCase("""
                enum DayOfWeek {
                    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
                }
                
                public class HolidayClass {
                   
                    public boolean isHoliday(DayOfWeek day) {
                        return true;
                    }
                }
                """);

        addIncorrectTestCase("""
                enum DayOfWeek {
                    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
                }
                
                public class HolidayClass {
                   
                    public boolean isHoliday(DayOfWeek day) {
                        return day == DayOfWeek.SATURDAY;
                    }
                }
                """);
    }
}
