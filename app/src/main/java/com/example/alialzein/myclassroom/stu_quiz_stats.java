package com.example.alialzein.myclassroom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 4/28/2018.
 */

public class stu_quiz_stats {
    private List<String> studentAnswers=new ArrayList<String>();
    private String id;

    public stu_quiz_stats(List<String> studentAnswers, String id) {
        this.studentAnswers = studentAnswers;
        this.id = id;
    }

    public List<String> getStudentAnswers() {
        return studentAnswers;
    }

    public String getId() {
        return id;
    }

    public void setStudentAnswers(List<String> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    public void setId(String id) {
        this.id = id;
    }
}
