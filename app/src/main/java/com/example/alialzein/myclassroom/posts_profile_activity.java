package com.example.alialzein.myclassroom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class posts_profile_activity extends AppCompatActivity {

    private Intent classroom_information;
    private String classroom_name, classroom_section, classroom_semester, UniqueClassId, instructorId;
    private Button comment;
    private boolean isInstructor;
    private ImageButton select_image_imagebtn, add_post_imagebtn;
    private EditText post_editText;
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private String onlineUser;
    private LinearLayout send_bar;
    private RecyclerView posts_list_recyclerView;
    private final List<Posts> postsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private PostsAdapter postsAdapter;
    private String inst_email;
    private static int Gallery_Pick = 1;
    private StorageReference message_image_storage_ref;
    private ProgressDialog progressDialog;

    private DatabaseReference ClassRoomsReference;
    private DatabaseReference classArrangmentRef;
   private Map instructor_classroom_info;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_profile_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        classroom_information = getIntent();
        instructorId = classroom_information.getStringExtra("instructorId");
        UniqueClassId = classroom_information.getStringExtra("UniqueClassId");
        classroom_name = classroom_information.getStringExtra("classroom_name");
        classroom_section = classroom_information.getStringExtra("classroom_section");
        classroom_semester = classroom_information.getStringExtra("classroom_semester");
        Log.i("classroominfo", classroom_name + " " + classroom_semester + " " + classroom_section + " " + UniqueClassId);
        setTitle(classroom_name);
        select_image_imagebtn = (ImageButton) findViewById(R.id.select_image_imageBtn);
        add_post_imagebtn = (ImageButton) findViewById(R.id.add_post_imageBtn);
        post_editText = (EditText) findViewById(R.id.post_message);
        send_bar = (LinearLayout) findViewById(R.id.send_bar);
        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        onlineUser = mAuth.getCurrentUser().getUid();
        message_image_storage_ref = FirebaseStorage.getInstance().getReference().child("messages_pictures");


        progressDialog = new ProgressDialog(this);

        //for recyclerView
        posts_list_recyclerView = (RecyclerView) findViewById(R.id.post_list_recyclerView);
        postsAdapter = new PostsAdapter(postsList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);//to reverse the layout
        linearLayoutManager.setStackFromEnd(true);
        posts_list_recyclerView.setHasFixedSize(true);
        posts_list_recyclerView.setLayoutManager(linearLayoutManager);
        posts_list_recyclerView.setAdapter(postsAdapter);

        FetchPosts();

        if (instructorId != null) {//the instructor id comes when the student enter the classroom,so when the student inter the insId!=null
            isInstructor = false;
        } else {
            isInstructor = true;
        }


        if (isInstructor) {//the interface of instructor
          //  Toast.makeText(this, "This is instructor", Toast.LENGTH_SHORT).show();

            add_post_imagebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addPost();
                }
            });

            select_image_imagebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, Gallery_Pick);
                }
            });


            inst_email = mAuth.getCurrentUser().getEmail();



        } else//the interface of students
        {
          //  Toast.makeText(this, "This is student", Toast.LENGTH_SHORT).show();
            send_bar.setVisibility(View.GONE);

            DatabaseReference inst_Ref = FirebaseDatabase.getInstance().getReference().child("instructors");
            inst_Ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    inst_email = dataSnapshot.child(instructorId).child("email").getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        if (!isInstructor) {
            getMenuInflater().inflate(R.menu.notification,menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.set_notification) {

            DatabaseReference localNotificationRef = FirebaseDatabase.getInstance().getReference().child("local_notification");
            localNotificationRef.child(UniqueClassId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String notf_time = dataSnapshot.child("class_time").getValue().toString();
                    String notf_id= dataSnapshot.child("notification_id").getValue().toString();

                    String[] splitted_time = new String[2];
                    splitted_time=notf_time.split(":");
                    int hour =Integer.valueOf( splitted_time[0]);
                    int min =Integer.valueOf( splitted_time[1]);
                    int id = Integer.valueOf(notf_id);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,hour);
                    calendar.set(Calendar.MINUTE,min);

                    Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);
                    intent.putExtra("id", id);
                    intent.putExtra("class_name", classroom_name);
                    intent.putExtra("UniqueOfClassroom", UniqueClassId);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                    Toast.makeText(posts_profile_activity.this, "The notification is at "+notf_time, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            progressDialog.setTitle("sending the image");
            progressDialog.setMessage("please wait ...");
            progressDialog.show();
            Uri imageUri = data.getData();


            final String post_ref = "posts/" + UniqueClassId;
            DatabaseReference postRef_key = rootRef.child("posts").child(UniqueClassId).push();
             postRef_key.keepSynced(true);
            final String post_push_id = postRef_key.getKey();
            StorageReference filePath = message_image_storage_ref.child(post_push_id + ".jpg");


            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                        final String downloadUrl = task.getResult().getDownloadUrl().toString();

                        Map postTextBody = new HashMap();
                        postTextBody.put("message", downloadUrl);
                        postTextBody.put("seen", false);
                        postTextBody.put("type", "image");
                        postTextBody.put("time", ServerValue.TIMESTAMP);
                        postTextBody.put("email", inst_email);
                        postTextBody.put("classroom_id", UniqueClassId);
                        postTextBody.put("post_push_id", post_push_id);

                        Map postBodyDetails = new HashMap();
                        postBodyDetails.put(post_ref + "/" + post_push_id, postTextBody);
                        rootRef.updateChildren(postBodyDetails, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("postLog", databaseError.getMessage().toString());
                                }
                                post_editText.setText("");
                                progressDialog.dismiss();
                            }
                        });
                        Toast.makeText(posts_profile_activity.this, "picture sent successfully.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(posts_profile_activity.this, "picture not sent, try again.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });


        }
    }

    private void FetchPosts() {
        rootRef.child("posts").child(UniqueClassId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Posts posts = dataSnapshot.getValue(Posts.class);
                postsList.add(posts);

                postsAdapter.notifyDataSetChanged();
                postsAdapter.notifyItemChanged(  postsAdapter.getItemCount());

                // Scroll to bottom on new messages
                posts_list_recyclerView.scrollToPosition(postsList.size() - 1);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addPost() {
        String post_text = post_editText.getText().toString();

        if (TextUtils.isEmpty(post_text)) {
            Toast.makeText(posts_profile_activity.this, "please add a post", Toast.LENGTH_SHORT).show();
        } else {
            String post_ref = "posts/" + UniqueClassId;
            DatabaseReference postRef_key = rootRef.child("posts").child(UniqueClassId).push();
            String post_push_id = postRef_key.getKey();

            Map postTextBody = new HashMap();
            postTextBody.put("message", post_text);
            postTextBody.put("seen", false);
            postTextBody.put("type", "text");
            postTextBody.put("time", ServerValue.TIMESTAMP);
            postTextBody.put("email", inst_email);
            postTextBody.put("classroom_id", UniqueClassId);
            postTextBody.put("post_push_id", post_push_id);

            Map postBodyDetails = new HashMap();
            postBodyDetails.put(post_ref + "/" + post_push_id, postTextBody);

            rootRef.updateChildren(postBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("postLog", databaseError.getMessage().toString());
                    }
                    post_editText.setText("");
                }

            });
            DatabaseReference classArrangmentRef=FirebaseDatabase.getInstance().getReference().child("classroom_arrangment");
            classArrangmentRef.child(UniqueClassId).child("post_time").setValue(ServerValue.TIMESTAMP);

            DatabaseReference newPostFlag=FirebaseDatabase.getInstance().getReference().child("new_post_flag");
            newPostFlag.child(UniqueClassId).child("new_post").setValue(true);

        }
    }


}
