package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_5;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice5Task3CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-5-task-3", TaskCheckService.class);

        addCorrectTestCase("""
                import java.io.*;
                
                public class Program {
                     // Метод для зчитування файлу та виведення його вмісту на екран
                     public void readFileContent(String fileName) {
                         FileInputStream fileInputStream = null;
                         try {
                             // Створюємо об'єкт FileInputStream для зчитування файлу
                             fileInputStream = new FileInputStream(fileName);
             
                             int content;
             
                             // Читаємо файл по байтах, поки не досягнемо кінця файлу (-1)
                             while ((content = fileInputStream.read()) != -1) {
                                 // Виводимо прочитаний байт як символ
                                 System.out.print((char) content);
                             }
                         } catch (IOException e) {
                             System.out.println("Виникла помилка при читанні файлу: " + e.getMessage());
                         } finally {
                             try {
                                 // Закриваємо потік після завершення читання
                                 if (fileInputStream != null) {
                                     fileInputStream.close();
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
                     // Метод для зчитування файлу та виведення його вмісту на екран
                     public void readFileContent(String fileName) {
                         FileInputStream fileInputStream = null;
                         try {
                             // Створюємо об'єкт FileInputStream для зчитування файлу
                             fileInputStream = new FileInputStream(fileName);
             
                             int content;
             
                             // Читаємо файл по байтах, поки не досягнемо кінця файлу (-1)
                             while ((content = fileInputStream.read()) != -1) {
                                 // Виводимо прочитаний байт як символ
                                 System.out.print((char) content);
                             }
                         } catch (IOException e) {
                             System.out.println("Виникла помилка при читанні файлу: " + e.getMessage());
                         }
                     }
                 }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                
                public class Program {
                     // Метод для зчитування файлу та виведення його вмісту на екран
                     public void readFileContent(String fileName) {
                         FileInputStream fileInputStream = null;
                         try {
                             // Створюємо об'єкт FileInputStream для зчитування файлу
                             fileInputStream = new FileInputStream(fileName);
             
                             int content;
             
                             // Читаємо файл по байтах, поки не досягнемо кінця файлу (-1)
                             while ((content = fileInputStream.read()) != -1) {
                                 // Виводимо прочитаний байт як символ
                                 System.out.println((char) content);
                             }
                         } catch (IOException e) {
                             System.out.println("Виникла помилка при читанні файлу: " + e.getMessage());
                         } finally {
                             try {
                                 // Закриваємо потік після завершення читання
                                 if (fileInputStream != null) {
                                     fileInputStream.close();
                                 }
                             } catch (IOException e) {
                                 System.out.println("Виникла помилка при закритті файлу: " + e.getMessage());
                             }
                         }
                     }
                 }
                """);
    }
}
