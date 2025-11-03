package com.k1ts.check.taskcheckservice.year_2024.course_3.cpp.practice_5;

import com.k1ts.check.taskcheckservice.DefaultTaskCheckServiceTests;
import com.k1ts.check.taskcheckservice.TaskCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Year2024Course3CppPractice5Task1CheckServiceTests extends DefaultTaskCheckServiceTests {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        taskCheckService = applicationContext.getBean("year-2024-course-3-cpp-practice-5-task-1", TaskCheckService.class);

        addCorrectTestCase("""
                import java.io.*;
                                
                public class Program {
                                
                        // Метод для копіювання вмісту файлу
                         public void copyFile(String sourceFile, String destinationFile) {
                             FileInputStream fis = null;
                             FileOutputStream fos = null;
                 
                             try {
                                 // Створюємо FileInputStream для читання з файлу-джерела
                                 fis = new FileInputStream(sourceFile);
                                 // Створюємо FileOutputStream для запису в файл-призначення
                                 fos = new FileOutputStream(destinationFile);
                 
                                 // Буфер для зберігання даних під час копіювання
                                 byte[] buffer = new byte[1024];
                                 int length;
                 
                                 // Читаємо дані з файлу-джерела і записуємо в файл-призначення
                                 while ((length = fis.read(buffer)) > 0) {
                                     fos.write(buffer, 0, length);
                                 }
                             } catch (IOException e) {
                                 System.err.println("Виникла помилка: " + e.getMessage());
                             } finally {
                                 // Закриваємо потоки, якщо вони відкриті
                                 try {
                                     if (fis != null) {
                                         fis.close();
                                     }
                                     if (fos != null) {
                                         fos.close();
                                     }
                                 } catch (IOException e) {
                                     System.err.println("Помилка при закритті потоків: " + e.getMessage());
                                 }
                             }
                         }
                    }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                                
                public class Program {
                                
                         // Метод для копіювання вмісту файлу
                         public void copyFile(String sourceFile, String destinationFile) {
                             FileInputStream fis = null;
                             FileOutputStream fos = null;
                 
                             try {
                                 // Створюємо FileInputStream для читання з файлу-джерела
                                 fis = new FileInputStream(sourceFile);
                                 // Створюємо FileOutputStream для запису в файл-призначення
                                 fos = new FileOutputStream(destinationFile);
                 
                                 // Буфер для зберігання даних під час копіювання
                                 byte[] buffer = new byte[1024];
                                 int length;
                 
                                 // Читаємо дані з файлу-джерела і записуємо в файл-призначення
                                 while ((length = fis.read(buffer)) > 0) {
                                     fos.write(buffer, 0, length);
                                 }
                 
                                 System.out.println("Копіювання завершено!");
                 
                             } catch (IOException e) {
                                 System.err.println("Виникла помилка: " + e.getMessage());
                             } finally {
                                 // Закриваємо потоки, якщо вони відкриті
                                 try {
                                     if (fis != null) {
                                         fis.close();
                                     }
                                     if (fos != null) {
                                         fos.close();
                                     }
                                 } catch (IOException e) {
                                     System.err.println("Помилка при закритті потоків: " + e.getMessage());
                                 }
                             }
                         }
                    }
                """);

        addIncorrectTestCase("""
                import java.io.*;
                                
                public class Program {
                                
                        // Метод для копіювання вмісту файлу
                         public void copyFile(String sourceFile, String destinationFile) {
                             FileInputStream fis = null;
                             FileOutputStream fos = null;
                 
                             try {
                                 // Створюємо FileInputStream для читання з файлу-джерела
                                 fis = new FileInputStream(sourceFile);
                                 // Створюємо FileOutputStream для запису в файл-призначення
                                 fos = new FileOutputStream(destinationFile);
                 
                                 // Буфер для зберігання даних під час копіювання
                                 byte[] buffer = new byte[1024];
                                 int length;
                 
                                 // Читаємо дані з файлу-джерела і записуємо в файл-призначення
                                 while ((length = fis.read(buffer)) > 0) {
                                     fos.write(buffer, 0, length);
                                 }
                 
                                 System.out.println("Копіювання завершено!");
                 
                             } catch (IOException e) {
                                 System.err.println("Виникла помилка: " + e.getMessage());
                             }
                         }
                    }
                """);
    }
}
