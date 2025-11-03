package com.k1ts.helper;

import com.k1ts.user.Role;
import com.k1ts.user.User;
import com.k1ts.user.UserConstants;
import com.k1ts.user.UserService;
import com.k1ts.user.studentdata.StudentData;
import com.k1ts.user.studentdata.StudentDataService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class UserHelperService {
    private final UserService userService;
    private final StudentDataService studentDataService;
    private final PasswordEncoder passwordEncoder;

    public String generateUser() {
        User user = User
                .builder()
                .username("derekdoe@gmail.com")
                .firstName("Derek")
                .lastName("Doe")
                .email("derekdoe@gmail.com")
                .password(passwordEncoder.encode("derekdoe@gmail.com"))
                .build();

        return userService.save(user).getUsername();
    }

    public String generateAdmin() {
        String username = DataGenerator.generateEmail();

        User admin = User
                .builder()
                .username(username)
                .role(Role.Admin)
                .firstName(DataGenerator.generateRandomString(UserConstants.FIRST_NAME_MIN_SIZE, UserConstants.FIRST_NAME_MAX_SIZE))
                .lastName(DataGenerator.generateRandomString(UserConstants.LAST_NAME_MIN_SIZE, UserConstants.LAST_NAME_MAX_SIZE))
                .email(username)
                .password(passwordEncoder.encode(username))
                .build();

        return userService.save(admin).getUsername();
    }

    public String generateStudent() {
        String username = DataGenerator.generateEmail();

        User student = userService.save(User
                .builder()
                .username(username)
                .role(Role.Student)
                .firstName(DataGenerator.generateRandomString(UserConstants.FIRST_NAME_MIN_SIZE, UserConstants.FIRST_NAME_MAX_SIZE))
                .lastName(DataGenerator.generateRandomString(UserConstants.LAST_NAME_MIN_SIZE, UserConstants.LAST_NAME_MAX_SIZE))
                .email(username)
                .password(passwordEncoder.encode(username))
                .build());

        studentDataService.save(StudentData
                .builder()
                .student(student)
                .firstCourseDate(LocalDate.of(2023, 9, 1))
                .speciality(DataGenerator.generateRandomNumber(UserConstants.SPECIALITIES))
                .build());

        return student.getUsername();
    }

    public String generateStudent(StudentParams params) {
        User student = userService.save(User
                .builder()
                .username(params.getUsername())
                .password(getPassword(params))
                .role(Role.Student)
                .firstName(params.getFirstName())
                .patronymic(params.getPatronymic())
                .lastName(params.getLastName())
                .email(params.getUsername())
                .build());

        studentDataService.save(StudentData
                .builder()
                .student(student)
                .firstCourseDate(params.getFirstCourseDate())
                .speciality(params.getSpeciality())
                .build());

        return student.getUsername();
    }

    private String getPassword(StudentParams studentParams) {
        if (studentParams.getRawPassword() == null) {
            return passwordEncoder.encode(studentParams.getUsername());
        }

        return passwordEncoder.encode(studentParams.getRawPassword());
    }

    @Getter
    @Builder
    public static class StudentParams {
        @Builder.Default
        private final String username = DataGenerator.generateEmail();

        private final String rawPassword;

        @Builder.Default
        private final String firstName = DataGenerator.generateRandomString(
                UserConstants.FIRST_NAME_MIN_SIZE,
                UserConstants.FIRST_NAME_MAX_SIZE);

        @Builder.Default
        private final String patronymic = DataGenerator.generateRandomString(
                UserConstants.PATRONYMIC_MIN_SIZE,
                UserConstants.PATRONYMIC_MAX_SIZE);

        @Builder.Default
        private final String lastName = DataGenerator.generateRandomString(
                UserConstants.LAST_NAME_MIN_SIZE,
                UserConstants.LAST_NAME_MAX_SIZE);

        @Builder.Default
        private final LocalDate firstCourseDate = LocalDate.of(2023, 9, 1);

        @Builder.Default
        private final int speciality = DataGenerator.generateRandomNumber(UserConstants.SPECIALITIES);
    }
}
