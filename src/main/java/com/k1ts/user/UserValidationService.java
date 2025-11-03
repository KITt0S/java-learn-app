package com.k1ts.user;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Pattern;

@Service
public class UserValidationService {
private final Pattern emailPattern = Pattern.compile("^.+@.+$");

    public boolean isEmailInvalid(String email) {
        if (email.isEmpty()) {
            return true;
        }

        return !emailPattern.matcher(email).matches();
    }

    public boolean isFirstNameInvalid(String firstName) {
        if (firstName.isEmpty()) {
            return true;
        }

        if (firstName.length() < UserConstants.FIRST_NAME_MIN_SIZE) {
            return true;
        }

        return firstName.length() > UserConstants.FIRST_NAME_MAX_SIZE;
    }

    public boolean isPatronymicInvalid(String patronymic) {
        if (patronymic.isEmpty()) {
            return true;
        }

        if (patronymic.length() < UserConstants.PATRONYMIC_MIN_SIZE) {
            return true;
        }

        return patronymic.length() > UserConstants.PATRONYMIC_MAX_SIZE;
    }

    public boolean isLastNameInvalid(String lastName) {
        if (lastName.isEmpty()) {
            return true;
        }

        if (lastName.length() < UserConstants.LAST_NAME_MIN_SIZE) {
            return true;
        }

        return lastName.length() > UserConstants.LAST_NAME_MAX_SIZE;
    }

    public boolean isPasswordInvalid(String password) {
        if (password.isEmpty()) {
            return true;
        }

        if (password.length() < UserConstants.PASSWORD_MIN_SIZE) {
            return true;
        }

        return password.length() > UserConstants.PASSWORD_MAX_SIZE;
    }

    public boolean isCourseNumberInvalid(int courseNumber) {
        if (courseNumber < UserConstants.MIN_COURSE_NUMBER) {
            return true;
        }

        return courseNumber > UserConstants.MAX_COURSE_NUMBER;
    }

    public boolean isSpecialityInvalid(int speciality) {
        return Arrays.stream(UserConstants.SPECIALITIES).noneMatch(value -> value == speciality);
    }
}
