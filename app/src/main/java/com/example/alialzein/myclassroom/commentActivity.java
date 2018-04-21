package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class commentActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private Intent get_post_info;
    private SimpleGestureFilter detector;
    private String classroom_id, post_push_id;
    private ImageButton add_comment_btn;
    private EditText comment_message;
    private DatabaseReference commentsRef;
    private FirebaseAuth mAuth;
    private  String sender_name,sender_id;
    private Map commentInfo;
    private DatabaseReference rootRef;
    private RecyclerView comments_recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference getCommentsRef;
    private Query commentsRefOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setTitle("Comments");

        detector = new SimpleGestureFilter(this,this);
        get_post_info = getIntent();
        classroom_id= get_post_info.getStringExtra("classroom_id");
        post_push_id= get_post_info.getStringExtra("post_push_id");
       // Toast.makeText(this, "classroom_id"+classroom_id+"post_push_id"+post_push_id, Toast.LENGTH_SHORT).show();

        add_comment_btn = (ImageButton) findViewById(R.id.add_comment_imageBtn);
        comment_message = (EditText) findViewById(R.id.comment_message);
        commentsRef= FirebaseDatabase.getInstance().getReference().child("comments").child(classroom_id).child(post_push_id);
        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        sender_id = mAuth.getCurrentUser().getUid();

        comments_recyclerview = (RecyclerView) findViewById(R.id.comments_recyclerview);

        linearLayoutManager = new LinearLayoutManager(commentActivity.this);

        comments_recyclerview.setLayoutManager(linearLayoutManager);
        commentsRefOrder = commentsRef.orderByChild("time");


        DatabaseReference allUsersRef = FirebaseDatabase.getInstance().getReference().child("users").child(sender_id);
        allUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sender_name = dataSnapshot.child("name").getValue().toString();

                add_comment_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addComment();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









    }

    private void addComment() {
        String comment_message_text = comment_message.getText().toString();

        if (TextUtils.isEmpty(comment_message_text)) {
            Toast.makeText(commentActivity.this, "please add a post", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference commentRef_key = commentsRef.push();
            String comment_push_key = commentRef_key.getKey();
            Map commentInfo = new HashMap();
            commentInfo.put("message", comment_message_text);
            commentInfo.put("time", ServerValue.TIMESTAMP);
            commentInfo.put("name", sender_name);
            commentInfo.put("sender_id", sender_id);

            Map commentBodyDetails = new HashMap();
            commentBodyDetails.put( comment_push_key, commentInfo);
            commentsRef.updateChildren(commentBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("postLog", databaseError.getMessage().toString());
                    }
                    comment_message.setText("");
                    onStart();
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<commentsClass,commentsViewHolder>firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<commentsClass, commentsViewHolder>
                        (commentsClass.class,
                                R.layout.comments_layout_display,
                                commentsViewHolder.class,
                                commentsRefOrder



                        )
                {
                    @Override
                    protected void populateViewHolder(commentsViewHolder viewHolder, commentsClass model, int position) {
                        viewHolder.setMessage(model.getMessage());
                        viewHolder.setName(model.getName());
                        viewHolder.setSender_id(model.getSender_id());


                        String Sender_id = model.getSender_id();


                        Date netDate = (new Date(model.getTime()));
                        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                        String time= formatter.format(netDate);
                        viewHolder.setTime(time);

                        if (sender_id.equals(Sender_id)) {
                            viewHolder.mView.findViewById(R.id.all_component).setBackgroundColor(getResources().getColor(R.color.whiteblue));
                               LinearLayout x= viewHolder.mView.findViewById(R.id.all_component);
                               x.setGravity(Gravity.RIGHT);

                        }


                    }
                };



        comments_recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class commentsViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public commentsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setMessage(String message) {
            TextView messagetxt = (TextView) mView.findViewById(R.id.comment_text);
            messagetxt.setText(message);

        }
        public void setName(String name) {
            TextView nametxt = (TextView) mView.findViewById(R.id.sender_name);
            nametxt.setText(name);


        }
        public void setSender_id(String sender_id) {

        }
        public void setTime(String time) {
            TextView timetxt = (TextView) mView.findViewById(R.id.comment_time);

            timetxt.setText(time);
        }


    }





    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN :
                finish();
                overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);
                str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDoubleTap() {

        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
