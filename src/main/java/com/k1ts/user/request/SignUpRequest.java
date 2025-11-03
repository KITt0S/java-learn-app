package com.k1ts.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String firstName;
    private String patronymic;
    private String lastName;
    private String email;
    private String password;
    private int courseNumber;
    private int speciality;
}
