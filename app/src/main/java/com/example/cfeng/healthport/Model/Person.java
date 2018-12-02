package com.example.cfeng.healthport.Model;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Person {
    private String password;
    private CharSequence email;

    public Person() {

    }

    public Person(String password, CharSequence email) {
        this.password = password;
        this.email = email;
    }

    private static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public CharSequence getEmail() {
        return email;
    }

    public void setEmail(CharSequence email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean validInput(CharSequence email, String pass) {
        if (pass.trim().equals("") && isEmailValid(email)) {
            throw new IllegalArgumentException("Please enter all fields correctly");
        }
        if (pass.length() < 6) {
            throw new IllegalArgumentException("Password cannot be less than six characters");
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
        }

        if (!hasUpper) {
            throw new IllegalArgumentException("Password must contain an upper case letter");
        }
        if (!hasLower) {
            throw new IllegalArgumentException("Password must contain a lower case letter");
        }
        if (!hasDigit) {
            throw new IllegalArgumentException("Password must contain a digit");
        }
        return true;
    }
}
