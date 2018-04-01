package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class instructor_Classroom_Profile extends AppCompatActivity {

    private Intent classroom_information;
    private String classroom_name,classroom_section,classroom_semester,UniqueClassId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor__classroom__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        classroom_information = getIntent();
        UniqueClassId = classroom_information.getStringExtra("UniqueClassId");
        classroom_name = classroom_information.getStringExtra("classroom_name");
        classroom_section = classroom_information.getStringExtra("classroom_section");
        classroom_semester = classroom_information.getStringExtra("classroom_semester");
        Log.i("classroominfo", classroom_name+" "+classroom_semester+" "+classroom_section+" "+UniqueClassId);

        setTitle(classroom_name);
    }

}
