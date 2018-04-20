package com.example.alialzein.myclassroom;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentClassroomFragment extends Fragment {
    private View myView;
    private FirebaseAuth mAuth;
    private RecyclerView student_classroom_list;
    private DatabaseReference student_classrooms_reference;
    private String studentId;
    private String instructorId;


    public StudentClassroomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       myView= inflater.inflate(R.layout.fragment_student_classroom, container, false);

        mAuth = FirebaseAuth.getInstance();

        student_classroom_list = (RecyclerView) myView.findViewById(R.id.student_classrooms_list);
        studentId = mAuth.getCurrentUser().getUid();
        student_classrooms_reference = FirebaseDatabase.getInstance().getReference().child("studentClassrooms").child(studentId);
        student_classrooms_reference.keepSynced(true);
      //  Query x = student_classrooms_reference.orderByChild()

        student_classroom_list.setLayoutManager(new LinearLayoutManager(getContext()));

        return myView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<student_Classrooms,studentClassroomsViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<student_Classrooms,studentClassroomsViewHolder>
                (       student_Classrooms.class,
                        R.layout.instructor_classrooms_display,
                        studentClassroomsViewHolder.class,
                        student_classrooms_reference


                )
        {
            @Override
            protected void populateViewHolder(StudentClassroomFragment.studentClassroomsViewHolder viewHolder, final student_Classrooms model, final int position) {

                viewHolder.setClassroom_name(model.getClassroom_name());
                viewHolder.setClassroom_section(model.getClassroom_section());
                viewHolder.setClassroom_semester(model.getClassroom_semester());
                viewHolder.setInstructorID(model.getInstructorID());



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        instructorId=model.getInstructorID();
                        String classroom_name = model.getClassroom_name();
                        String classroom_section = model.getClassroom_section();
                        String classroom_semester = model.getClassroom_semester();
                        String UniqueClassId = getRef(position).getKey();
                        Intent to_student_classroom_profile = new Intent(getActivity(), instructor_Classroom_Profile.class);
                        to_student_classroom_profile.putExtra("UniqueClassId", UniqueClassId);
                        to_student_classroom_profile.putExtra("instructorId", instructorId);
                        to_student_classroom_profile.putExtra("classroom_name", classroom_name);
                        to_student_classroom_profile.putExtra("classroom_section", classroom_section);
                        to_student_classroom_profile.putExtra("classroom_semester", classroom_semester);
                        startActivity(to_student_classroom_profile);
                        Log.i("testinst", instructorId);
                        Log.i("teststu", UniqueClassId);
                    }
                });
            }
        };

        student_classroom_list.setAdapter(firebaseRecyclerAdapter);
    }

    public static class studentClassroomsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public studentClassroomsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setClassroom_name(String classroom_name) {
            TextView class_name = (TextView) mView.findViewById(R.id.Classroom_name);
            class_name.setText(classroom_name);
        }
        public void setClassroom_section(String classroom_section) {

            TextView class_section = (TextView) mView.findViewById(R.id.Classroom_section);
            class_section.setText(classroom_section);
        }
        public void setClassroom_semester(String classroom_semester) {

            TextView class_semester= (TextView) mView.findViewById(R.id.Classroom_semester);
            class_semester.setText(classroom_semester);
        }
        public void setInstructorID(String instructorID) {


        }
    }

}
