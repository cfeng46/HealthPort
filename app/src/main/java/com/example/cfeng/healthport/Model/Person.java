package com.example.cfeng.healthport.Model;

import android.util.Patterns;

public class Person {
    private String username;
    private String password;
    private String email;

    public Person() {

    };

    public Person(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CharSequence getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static boolean validInput(String name, String email, String pass) {
        if (name.trim().equals("") || pass.trim().equals("") || email.trim().equals("")) {
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
