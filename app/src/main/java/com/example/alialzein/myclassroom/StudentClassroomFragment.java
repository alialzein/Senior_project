package com.example.alialzein.myclassroom;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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
    DatabaseReference studentClassroomsRef;
    private Query classroom_orders;
    private LinearLayoutManager linearLayoutManager;


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
         classroom_orders = student_classrooms_reference.orderByChild("post_time");

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);//to reverse the layout
        linearLayoutManager.setStackFromEnd(true);

        student_classroom_list.setLayoutManager(linearLayoutManager);

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
                        classroom_orders


                )
        {
            @Override
            protected void populateViewHolder(final StudentClassroomFragment.studentClassroomsViewHolder viewHolder, final student_Classrooms model, final int position) {

                viewHolder.setClassroom_name(model.getClassroom_name());
                viewHolder.setClassroom_section(model.getClassroom_section());
                viewHolder.setClassroom_semester(model.getClassroom_semester());
                viewHolder.setInstructorID(model.getInstructorID());

                instructorId=model.getInstructorID();
                String classroom_name = model.getClassroom_name();
                String classroom_section = model.getClassroom_section();
                String classroom_semester = model.getClassroom_semester();
                final String UniqueClassId = getRef(position).getKey();

                ///////////////////////////////////////////////////////////////////
                DatabaseReference newPostFlag=FirebaseDatabase.getInstance().getReference().child("new_post_flag");
                newPostFlag.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(UniqueClassId)) {
                           boolean newPostFlag= dataSnapshot.child(UniqueClassId).child("new_post").getValue(boolean.class);

                            if (newPostFlag) {
                                viewHolder.mView.findViewById(R.id.all_background).setBackgroundColor(getResources().getColor(R.color.newPostFlag));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ///////////////////////////////////////////////////////////////////


                //////////////////////////////////////////////////////////////////////////////////////////////////
                //to update the post_time to latest post time
               studentClassroomsRef = FirebaseDatabase.getInstance().getReference().child("studentClassrooms").child(studentId);
                DatabaseReference classArrangmentRef=FirebaseDatabase.getInstance().getReference().child("classroom_arrangment");
                classArrangmentRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(UniqueClassId)) {
                            long time= (long)dataSnapshot.child(UniqueClassId).child("post_time").getValue();

                            Map student_classroom_info = new HashMap();

                            student_classroom_info.put("post_time", time);
                            studentClassroomsRef = FirebaseDatabase.getInstance().getReference().child("studentClassrooms").child(studentId);

                            studentClassroomsRef.child(UniqueClassId).updateChildren(student_classroom_info);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                /////////////////////////////////////////////////////////////////////////////////////////////////////



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        instructorId=model.getInstructorID();
                        String classroom_name = model.getClassroom_name();
                        String classroom_section = model.getClassroom_section();
                        String classroom_semester = model.getClassroom_semester();
                        String UniqueClassId = getRef(position).getKey();
                        Intent to_student_classroom_profile = new Intent(getActivity(), posts_profile_activity.class);
                        to_student_classroom_profile.putExtra("UniqueClassId", UniqueClassId);
                        to_student_classroom_profile.putExtra("instructorId", instructorId);
                        to_student_classroom_profile.putExtra("classroom_name", classroom_name);
                        to_student_classroom_profile.putExtra("classroom_section", classroom_section);
                        to_student_classroom_profile.putExtra("classroom_semester", classroom_semester);
                        startActivity(to_student_classroom_profile);
                        Log.i("testinst", instructorId);
                        Log.i("teststu", UniqueClassId);

                        DatabaseReference newPostFlag=FirebaseDatabase.getInstance().getReference().child("new_post_flag");
                        newPostFlag.child(UniqueClassId).child("new_post").setValue(false);
                    }
                });

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    String UniqueClassId = getRef(position).getKey();
                    @Override
                    public boolean onLongClick(View view) {
                        new MaterialDialog.Builder(getActivity())
                                .title("Do you want to delete the classroom ?")
                                .positiveText("Yes")
                                .negativeText("No")
                                .theme(Theme.DARK)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        DatabaseReference studentClassroomsRef=FirebaseDatabase.getInstance().getReference()
                                                .child("studentClassrooms");
                                        studentClassroomsRef.child(studentId).child(UniqueClassId).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(getActivity(), "ClassRoom Removed Successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });



                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    }
                                })
                                .show();
                        return false;
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
