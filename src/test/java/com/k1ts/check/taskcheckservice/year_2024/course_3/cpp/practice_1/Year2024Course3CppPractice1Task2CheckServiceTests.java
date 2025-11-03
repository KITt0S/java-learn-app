package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_1;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice1Task2CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-1-task-2", TaskCheckService.class);

        addCorrectTestCase("""                
                public class ChildThreadApp {
                        public static void main(String[] args) {
                            Thread firstThread = new Thread(() ->
                                    System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getPriority()));
                                
                            firstThread.setName("Дочірній потік");
                            firstThread.setPriority(Thread.MAX_PRIORITY);
                                
                            firstThread.start();
                        }
                    }
                """);

        addIncorrectTestCase("""
                public class ChildThreadApp {
                        public static void main(String[] args) {
                            Thread firstThread = new Thread(() ->
                                    System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getPriority()));
                                
                            firstThread.start();
                        }
                    }
                """);

        addIncorrectTestCase("""
                public class ChildThreadApp {
                        public static void main(String[] args) {
                            Thread firstThread = new Thread(() ->
                                    System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getPriority()));
                                
                            firstThread.setName("Дочірній потік");
                            firstThread.setPriority(Thread.MIN_PRIORITY);
                                
                            firstThread.start();
                        }
                    }
                """);
    }
}
