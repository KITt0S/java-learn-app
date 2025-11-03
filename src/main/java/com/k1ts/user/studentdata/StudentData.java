package com.k1ts.user.studentdata;

import com.k1ts.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class StudentData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "student_username")
    private User student;

    private int speciality;

    private LocalDate firstCourseDate;
}
