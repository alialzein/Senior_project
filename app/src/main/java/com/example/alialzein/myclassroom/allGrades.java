package com.example.alialzein.myclassroom;

public class allGrades {

    public String email;
    public String grade;

    public allGrades() {
    }

    public allGrades(String email, String grade) {
        this.email = email;
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

