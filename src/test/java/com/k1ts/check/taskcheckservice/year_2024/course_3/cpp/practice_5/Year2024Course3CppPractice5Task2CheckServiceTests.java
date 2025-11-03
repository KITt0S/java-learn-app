package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_5;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice5Task2CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-5-task-2", TaskCheckService.class);

        addCorrectTestCase("""
                import java.io.*;
                
                public class Program {
                   
                       // Метод для запису тексту у файл
                       public void writeTextToFile(String fileName, String text) {
                           FileOutputStream fileOutputStream = null;
                           try {
                               // Створюємо об'єкт FileOutputStream для запису у файл
                               fileOutputStream = new FileOutputStream(fileName);
               
                               // Перетворюємо текст на масив байтів і записуємо до файлу
                               fileOutputStream.write(text.getBytes());
                           } catch (IOException e) {
                               System.out.println("Виникла помилка при записі файлу: " + e.getMessage());
                           } finally {
                               try {
                                   // Закриваємо потік після завершення запису
                                   if (fileOutputStream != null) {
                                       fileOutputStream.close();
                                   }
                               } catch (IOException e) {
                                   System.out.println("Виникла помилка при закритті файлу: " + e.getMessage());
                               }
                           }
                       }
                   }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                
                public class Program {
                   
                       // Метод для запису тексту у файл
                       public void writeTextToFile(String fileName, String text) {
                           FileOutputStream fileOutputStream = null;
                           try {
                               // Створюємо об'єкт FileOutputStream для запису у файл
                               fileOutputStream = new FileOutputStream(fileName);
               
                               // Перетворюємо текст на масив байтів і записуємо до файлу
                               fileOutputStream.write(text.getBytes());
                               System.out.println("Текст успішно записано до файлу: " + fileName);
                           } catch (IOException e) {
                               System.out.println("Виникла помилка при записі файлу: " + e.getMessage());
                           } finally {
                               try {
                                   // Закриваємо потік після завершення запису
                                   if (fileOutputStream != null) {
                                       fileOutputStream.close();
                                   }
                               } catch (IOException e) {
                                   System.out.println("Виникла помилка при закритті файлу: " + e.getMessage());
                               }
                           }
                       }
                   }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                
                public class Program {
                   
                       // Метод для запису тексту у файл
                       public void writeTextToFile(String fileName, String text) {
                           FileOutputStream fileOutputStream = null;
                           try {
                               // Створюємо об'єкт FileOutputStream для запису у файл
                               fileOutputStream = new FileOutputStream(fileName);
               
                               // Перетворюємо текст на масив байтів і записуємо до файлу
                               fileOutputStream.write(text.getBytes());
                           } catch (IOException e) {
                               System.out.println("Виникла помилка при записі файлу: " + e.getMessage());
                           }
                       }
                   }
                """);
    }
}
