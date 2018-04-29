package com.example.alialzein.myclassroom;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorClassroomFragment extends Fragment {
private FloatingActionButton Create_New_Classroom;
private View myView;
private FirebaseAuth mAuth;
private RecyclerView instructor_classroom_list;
private DatabaseReference Classrooms_reference;
private String instructorId;
    private DatabaseReference classArrangmentRef;
    private Map instructor_classroom_info;
    private Query classroom_orders;
    private LinearLayoutManager linearLayoutManager;
    private int notfId;

    public InstructorClassroomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_instructor_classroom, container, false);
        mAuth = FirebaseAuth.getInstance();

        Create_New_Classroom = myView.findViewById(R.id.create_new_classroom_btn);
        try {
            Create_New_Classroom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                startActivity(new Intent(getActivity(),Create_Classroom.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        instructor_classroom_list = (RecyclerView) myView.findViewById(R.id.instructor_classrooms_list);
        instructorId = mAuth.getCurrentUser().getUid();
        Classrooms_reference = FirebaseDatabase.getInstance().getReference().child("classrooms").child(instructorId);
        Classrooms_reference.keepSynced(true);

        classroom_orders = Classrooms_reference.orderByChild("post_time");

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);//to reverse the layout
        linearLayoutManager.setStackFromEnd(true);




        instructor_classroom_list.setLayoutManager(linearLayoutManager);






        // Inflate the layout for this fragment
        return myView;


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Instructor_Classrooms,InstructorClassrromsViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<Instructor_Classrooms, InstructorClassrromsViewHolder>
                (Instructor_Classrooms.class,
                        R.layout.instructor_classrooms_display,
                        InstructorClassrromsViewHolder.class,
                        classroom_orders


                )
        {
            @Override
            protected void populateViewHolder(InstructorClassrromsViewHolder viewHolder, final Instructor_Classrooms model, final int position) {

                viewHolder.setClassroom_name(model.getClassroom_name());
                viewHolder.setClassroom_section(model.getClassroom_section());
                viewHolder.setClassroom_semester(model.getClassroom_semester());

                final String UniqueClassId = getRef(position).getKey();




                ////////////////////////////////////////////////////////////////////////////////////////////////
                //to update the post_time to latest post time
                classArrangmentRef=FirebaseDatabase.getInstance().getReference().child("classroom_arrangment");
                classArrangmentRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(UniqueClassId)) {
                            Long time= dataSnapshot.child(UniqueClassId).child("post_time").getValue(long.class);

                            instructor_classroom_info = new HashMap();

                            instructor_classroom_info.put("post_time", time);
                            Classrooms_reference.child(UniqueClassId).updateChildren(instructor_classroom_info);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ///////////////////////////////////////////////////////////////////////////////////////////////////




                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String classroom_name = model.getClassroom_name();
                        String classroom_section = model.getClassroom_section();
                        String classroom_semester = model.getClassroom_semester();
                        String UniqueClassId = getRef(position).getKey();
                        Intent to_instructor_classroom_profile = new Intent(getActivity(), posts_profile_activity.class);

                        to_instructor_classroom_profile.putExtra("UniqueClassId", UniqueClassId);
                        to_instructor_classroom_profile.putExtra("classroom_name", classroom_name);
                        to_instructor_classroom_profile.putExtra("classroom_section", classroom_section);
                        to_instructor_classroom_profile.putExtra("classroom_semester", classroom_semester);
                        startActivity(to_instructor_classroom_profile);

                        Log.i("teststu", UniqueClassId);
                    }
                });

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
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

                                        DatabaseReference ClassroomsRef=FirebaseDatabase.getInstance().getReference()
                                                .child("classrooms");
                                        final DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
                                        ClassroomsRef.child(instructorId).child(UniqueClassId).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    postsRef.child(UniqueClassId).removeValue();
                                                }
                                            }
                                        });
                                        DatabaseReference localNotificationRef = FirebaseDatabase.getInstance().getReference().child("local_notification");
                                        localNotificationRef.child(UniqueClassId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("notification_id")) {
                                                    String ntfid=  dataSnapshot.child("notification_id").getValue().toString();
                                                    notfId = Integer.valueOf(ntfid);
                                                    Intent intent = new Intent(getActivity().getApplicationContext(), Notification_reciever.class);
                                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), notfId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                                                    alarmManager.cancel(pendingIntent);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                       // Toast.makeText(getContext(), "alarm removed", Toast.LENGTH_SHORT).show();



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

        instructor_classroom_list.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public static class InstructorClassrromsViewHolder extends RecyclerView.ViewHolder {
      View mView;

        public InstructorClassrromsViewHolder(View itemView) {
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
    }

}
