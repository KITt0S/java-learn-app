package com.k1ts.user.studentdata;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StudentFirstCourseDateDefiner {

    public LocalDate defineFirstCourseDate(int courseNumber) {
        LocalDate now = LocalDate.now();

        LocalDate january1 = LocalDate.of(now.getYear(), 1, 1);
        LocalDate august31 = LocalDate.of(now.getYear(), 8, 31);

        if (now.isEqual(january1) || now.isAfter(january1) && (now.isBefore(august31) || now.isEqual(august31))) {
            return LocalDate.of(now.getYear() - courseNumber, 9, 1);
        }

        return LocalDate.of(now.getYear() - courseNumber + 1, 9, 1);
    }
}
