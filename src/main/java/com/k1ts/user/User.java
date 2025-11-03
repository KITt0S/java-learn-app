package com.k1ts.user;

import com.k1ts.user.studentdata.StudentData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    private String username;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String firstName;

    private String patronymic;

    private String lastName;

    private String email;

    private String password;

    private boolean verified;

    @OneToOne(mappedBy = "student", cascade = CascadeType.REMOVE)
    private StudentData studentData;
}
