package com.example.alialzein.myclassroom;

import java.util.ArrayList;

/**
 * Created by ALiAlzein on 3/14/2018.
 */

public class QuizClass {
    ArrayList<QuestionClass> questions = new ArrayList<QuestionClass>();
    int numOfQuestions;
    String quizID;

    public QuizClass(String quizID,ArrayList<QuestionClass> questions, int numOfQuestions) {
        this.questions = questions;
        this.numOfQuestions = numOfQuestions;
    }

    public ArrayList<QuestionClass> getQuestions() {
        return questions;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public double QuizResult() {
        int correctAnswer=0;
        for(int i=0;i<numOfQuestions;i++) {
            if (questions.get(i).correctQuestion()) {
              correctAnswer++;

            }

        }
        double weightOfOneQuestion=100/numOfQuestions;
        double finalAnswer = weightOfOneQuestion * correctAnswer;
        return finalAnswer;
    }

}
