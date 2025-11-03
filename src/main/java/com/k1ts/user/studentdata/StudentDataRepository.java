package com.k1ts.user.studentdata;

import com.k1ts.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StudentDataRepository extends CrudRepository<StudentData, User> {

    @Query(nativeQuery = true, value = "SELECT first_course_date FROM student_data WHERE student_username = :studentUsername")
    LocalDate getFirstCourseDate(@Param("studentUsername") String studentUsername);

    @Query(nativeQuery = true, value = "SELECT * FROM student_data WHERE student_username = :studentUsername")
    StudentData getByUsername(@Param("studentUsername") String username);

    @Query(nativeQuery = true, value = "SELECT speciality FROM student_data WHERE student_username = :username")
    int getSpeciality(@Param("username") String username);
}
