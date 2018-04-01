package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class student_Classroom_Profile extends AppCompatActivity {
     private Intent classroom_information;
    private String instructorId,classroom_name,classroom_section,classroom_semester,UniqueClassId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__classroom__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        classroom_information = getIntent();
        UniqueClassId = classroom_information.getStringExtra("UniqueClassId");
        instructorId= classroom_information.getStringExtra("instructorId");
        classroom_name = classroom_information.getStringExtra("classroom_name");
        classroom_section = classroom_information.getStringExtra("classroom_section");
        classroom_semester = classroom_information.getStringExtra("classroom_semester");
        Log.i("classroominfo", instructorId+" "+classroom_name+" "+classroom_semester+" "+classroom_section+" "+UniqueClassId);

        setTitle(classroom_name);
    }

}
