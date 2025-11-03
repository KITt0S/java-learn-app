package com.k1ts.user.studentdata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class StudentDataService {
    private final StudentDataRepository studentDataRepository;

    public StudentData save(StudentData userCourse) {
        return studentDataRepository.save(userCourse);
    }

    public LocalDate getFirstCourseDate(String studentUsername) {
        return studentDataRepository.getFirstCourseDate(studentUsername);
    }

    public StudentData getByUsername(String username) {
        return studentDataRepository.getByUsername(username);
    }

    public int getSpeciality(String username) {
        return studentDataRepository.getSpeciality(username);
    }
}
