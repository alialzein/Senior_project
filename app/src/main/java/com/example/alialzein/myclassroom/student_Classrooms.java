package com.example.alialzein.myclassroom;

/**
 * Created by ALiAlzein on 4/1/2018.
 */

public class student_Classrooms {

    public String classroom_name;
    public String classroom_section;
    public String classroom_semester;
    public String instructorID;

    public student_Classrooms() {

    }

    public student_Classrooms(String classroom_name, String classroom_section, String classroom_semester,String instructorID) {
        this.classroom_name = classroom_name;
        this.classroom_section = classroom_section;
        this.classroom_semester = classroom_semester;
        this.instructorID = instructorID;

    }

    public String getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(String instructorID) {
        this.instructorID = instructorID;
    }

    public String getClassroom_name() {
        return classroom_name;
    }

    public void setClassroom_name(String classroom_name) {
        this.classroom_name = classroom_name;
    }

    public String getClassroom_section() {
        return classroom_section;
    }

    public void setClassroom_section(String classroom_section) {
        this.classroom_section = classroom_section;
    }

    public String getClassroom_semester() {
        return classroom_semester;
    }

    public void setClassroom_semester(String classroom_semester) {
        this.classroom_semester = classroom_semester;
    }
}
