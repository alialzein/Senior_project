package com.example.alialzein.myclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Create_Classroom extends AppCompatActivity {
    private Button add_Student_to_Classroom;
    private EditText classroom_name;
    private EditText classroom_section;
    private EditText classroom_semester;
   private String UniqueOfClassroom;
   private String Class_name,Class_section,Class_semester;
   private DatabaseReference ClassRoomsReference;
    private String ClassroomId;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private String instructorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__classroom);
        add_Student_to_Classroom = (Button) findViewById(R.id.add_student_to_classroom);
        classroom_name = (EditText) findViewById(R.id.classroom_name);
        classroom_section = (EditText) findViewById(R.id.classroom_section);
        classroom_semester = (EditText) findViewById(R.id.classroom_semester);

        mAuth = FirebaseAuth.getInstance();
        instructorID = mAuth.getCurrentUser().getUid();

        loadingBar = new ProgressDialog(this);
        ClassRoomsReference = FirebaseDatabase.getInstance().getReference().child("classrooms");
        ClassRoomsReference.keepSynced(true);


        add_Student_to_Classroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Class_name = classroom_name.getText().toString().toUpperCase();
                 Class_section = classroom_section.getText().toString().toUpperCase();
                 Class_semester = classroom_semester.getText().toString().toUpperCase();
                if (TextUtils.isEmpty(Class_name) || TextUtils.isEmpty(Class_section) || TextUtils.isEmpty(Class_semester))
                {
                    Toast.makeText(Create_Classroom.this,"You have Some Missing Fields",Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        UniqueOfClassroom=Class_name+"_"+Class_semester+"_"+Class_section;
                        SendUserToAddStudents();
                    }

            }
        });
    }

    private void SendUserToAddStudents() {
        loadingBar.setTitle("Creating ClassRoom");
        loadingBar.setMessage("Please Wait, while we are creating ClassRoom for you");
        loadingBar.show();
        Map classroom_info = new HashMap();
        String post_time = String.valueOf(ServerValue.TIMESTAMP);
        //  classroom_info.put("instructor_ID", instructorID);
        classroom_info.put("classroom_name", Class_name);
        classroom_info.put("classroom_semester", Class_semester);
        classroom_info.put("classroom_section", Class_section);
        classroom_info.put("post_time", ServerValue.TIMESTAMP);

        ClassRoomsReference.child(instructorID).child(UniqueOfClassroom).setValue(classroom_info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loadingBar.dismiss();
                    Toast.makeText(Create_Classroom.this, "ClassRoom Created Successfully", Toast.LENGTH_SHORT).show();



                }
            }
        });



        Intent toAddStudentActivity = new Intent(Create_Classroom.this, AllStudentsAccount.class);
        toAddStudentActivity.putExtra("Class_name", Class_name);
        toAddStudentActivity.putExtra("Class_section", Class_section);
        toAddStudentActivity.putExtra("Class_semester", Class_semester);

        startActivity(toAddStudentActivity);
        finish();



    }
}
