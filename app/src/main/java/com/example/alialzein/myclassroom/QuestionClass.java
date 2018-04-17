package com.example.alialzein.myclassroom;

import java.util.ArrayList;

/**
 * Created by ALiAlzein on 3/14/2018.
 */

public class QuestionClass {
     String question;
    ArrayList<String> choices=new ArrayList<>(4);
    int correctAnswer;
    int studentAnswer;

    public QuestionClass(String question, ArrayList choices, int correctAnswer) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public QuestionClass(String question, ArrayList choices, int correctAnswer, int studentAnswer) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
        this.studentAnswer = studentAnswer;
    }

    public QuestionClass() {
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public int getStudentAnswer() {
        return studentAnswer;
    }

    public boolean correctQuestion(){
        return studentAnswer==correctAnswer;

    }

}
