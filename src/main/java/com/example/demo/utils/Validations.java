package com.example.demo.utils;

import java.time.LocalDateTime;

public class Validations {
    private Validations() {

    }

    /*
    Email validation policy:
        can contain uppercase letters
        can contain lowercase letters
        can contain 0-9 numbers
        may contain only dot(.), dash(-) and underscore(_)
        leading, trailing, or consecutive dots are restricted
        number of characters in top level domain are restricted
     */
    public static boolean validateEmail(String email) {
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return email.matches(emailPattern);
    }

    /*
    Password validation policy:
        Must be at least 8 characters long
        Must contain at least one digit
        Must contain at least one uppercase letter and one lowercase letter
        Must contain at least one special character (@#%^&+=)
        Must not contain a whitespace
     */
    public static boolean validatePassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$)(.{8,})$";
        return password.matches(passwordPattern);
    }

    /*
    Birthdate validation policy:
        Must be older than 15 years
     */
    public static boolean validateDateOfBirth(LocalDateTime dateOfBirth) {
        return (LocalDateTime.now().getYear() - dateOfBirth.getYear() >= 15);
    }
}

