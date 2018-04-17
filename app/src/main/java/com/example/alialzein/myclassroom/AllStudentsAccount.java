package com.example.alialzein.myclassroom;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllStudentsAccount extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView allStudentsList;
    private Button finish_create_classroom;
    private DatabaseReference all_students_Ref;
    private DatabaseReference ClassRoomsReference;
    private DatabaseReference NotificationRef;
    private FirebaseAuth mAuth;
    private String instructorID;
    String student_id;
    private  String CURRENT_STUDENT_STATE="";
    private Button AddStudent;
    private String UniqueOfClassroom;
    private String Class_name,Class_section,Class_semester;
    private String ClassroomId;
    private ProgressDialog loadingBar;
    private int student_num=0;
    private DatabaseReference studentClassroomsRef;
    private Query Search_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_students_account);

        loadingBar = new ProgressDialog(this);
        allStudentsList = (RecyclerView) findViewById(R.id.all_students_list);
        finish_create_classroom = (Button) findViewById(R.id.finish_create_classroom);
        allStudentsList.setHasFixedSize(true);
        allStudentsList.setLayoutManager(new LinearLayoutManager(this));
        all_students_Ref = FirebaseDatabase.getInstance().getReference().child("students");
        all_students_Ref.keepSynced(true);
        ClassRoomsReference = FirebaseDatabase.getInstance().getReference().child("classrooms");
        ClassRoomsReference.keepSynced(true);
        NotificationRef = FirebaseDatabase.getInstance().getReference().child("notification");
        NotificationRef.keepSynced(true);


        mAuth = FirebaseAuth.getInstance();
        instructorID = mAuth.getCurrentUser().getUid();
        Intent createClassroomActivity = getIntent();
        Class_name = createClassroomActivity.getStringExtra("Class_name");
        Class_section = createClassroomActivity.getStringExtra("Class_section");
        Class_semester = createClassroomActivity.getStringExtra("Class_semester");
        UniqueOfClassroom=Class_name+"_"+Class_semester+"_"+Class_section;
        ClassroomId = instructorID;

        Log.i("UniqueOfClassroom", ClassroomId);

        finish_create_classroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllStudentsAccount.this,Instructor_board.class));
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void RemoveStudent() {
        ClassRoomsReference.child(ClassroomId).child(UniqueOfClassroom).child("Student_In_Classroom").child(student_id)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    studentClassroomsRef = FirebaseDatabase.getInstance().getReference().child("studentClassrooms").child(student_id);
                    studentClassroomsRef.child(UniqueOfClassroom).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AllStudentsAccount.this, "Student Removed Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });




                }
            }
        });

    }

    private void AddStudent() {
        ClassRoomsReference.child(ClassroomId).child(UniqueOfClassroom).child("Student_In_Classroom").child(student_id)
              .setValue(student_num).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Map student_classroom_info = new HashMap();

                    student_classroom_info.put("classroom_name", Class_name);
                    student_classroom_info.put("classroom_semester", Class_semester);
                    student_classroom_info.put("classroom_section", Class_section);
                    student_classroom_info.put("instructorID", instructorID);
                    studentClassroomsRef = FirebaseDatabase.getInstance().getReference().child("studentClassrooms").child(student_id);

                    studentClassroomsRef.child(UniqueOfClassroom).setValue(student_classroom_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    //to add notification refrence for realtime notification
                    //ref=notification/student_id/(id generated by push())/notificationData(from...)
                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from",instructorID);
                    NotificationRef.child(student_id).push().setValue(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(AllStudentsAccount.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });





                }
            }
        });


    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {


        return true;
    }


    public static class All_students_viewHolder extends RecyclerView.ViewHolder {
        View mView;
        public All_students_viewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView username = (TextView) mView.findViewById(R.id.all_students_username);
            username.setText(name);
        }

        public void setEmail(String email) {
            TextView useremail = (TextView) mView.findViewById(R.id.all_students_emails);
            useremail.setText(email);
        }

        public void setThumb_profile_image(final Context ctx, final String thumb_profile_image) {

            final CircleImageView userimage = (CircleImageView) mView.findViewById(R.id.all_students_profile_image);
           //
            Picasso.with(ctx).load(thumb_profile_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable
                    .default_profile_img).into(userimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(thumb_profile_image).placeholder(R.drawable.default_profile_img).into(userimage);
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchManager searchManager =(SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                Search_student=all_students_Ref.orderByChild("email")
                        .startAt(s)
                        .endAt(s+"\uf8ff");


                FirebaseRecyclerAdapter<All_Students,All_students_viewHolder> firebaseRecyclerAdapter
                        =new FirebaseRecyclerAdapter<All_Students, All_students_viewHolder>
                        (All_Students.class,
                                R.layout.all_students_diplay_layout,
                                All_students_viewHolder.class,
                                Search_student

                        )
                {
                    @Override
                    protected void populateViewHolder(final All_students_viewHolder viewHolder, All_Students model, final int position) {
                        viewHolder.setName(model.getName());
                        viewHolder.setEmail(model.getEmail());
                        viewHolder.setThumb_profile_image(AllStudentsAccount.this,model.getThumb_profile_image());
                        // to send a request to student

                        viewHolder.mView.findViewById(R.id.Add_student_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                student_id = getRef(position).getKey();
                                student_num++;
                                AddStudent();
                                viewHolder.mView.findViewById(R.id.Add_student_btn).setVisibility(View.INVISIBLE);
                                viewHolder.mView.findViewById(R.id.Remove_student_btn).setVisibility(View.VISIBLE);
                                Log.i("test", "inAdd");
                            }
                        });
                        //to remove student
                        viewHolder.mView.findViewById(R.id.Remove_student_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                student_id = getRef(position).getKey();
                                RemoveStudent();
                                viewHolder.mView.findViewById(R.id.Remove_student_btn).setVisibility(View.INVISIBLE);
                                viewHolder.mView.findViewById(R.id.Add_student_btn).setVisibility(View.VISIBLE);
                                Log.i("test", "inRemove");

                            }
                        });
                    }
                };
                allStudentsList.setAdapter(firebaseRecyclerAdapter);


                return false;
            }
        });
        return true;
    }



}
