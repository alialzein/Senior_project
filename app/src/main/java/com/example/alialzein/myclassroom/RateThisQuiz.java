package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateThisQuiz extends AppCompatActivity {
    private RatingBar RB;
    private Intent intent;
    private DatabaseReference quizRate;
    private FirebaseAuth mAuth;
    private String StudentID;
    private String RATE;
    private String Quizid;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_this_quiz);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Rate This Quiz");
        //setSupportActionBar(toolbar);
       intent= getIntent();
       Quizid=intent.getStringExtra("quizid");
       submit=(Button)findViewById(R.id.submitrate);
        RB=(RatingBar)findViewById(R.id.ratingBar2);
        RB.setNumStars(5);
        mAuth=FirebaseAuth.getInstance();
        StudentID=mAuth.getCurrentUser().getUid();
        quizRate= FirebaseDatabase.getInstance().getReference().child("quizrating");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RATE=String.valueOf(RB.getRating());
                quizRate.child(Quizid).child(StudentID).setValue(RATE).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(RateThisQuiz.this,Student_board.class));
                        }
                    }
                });
                finish();
            }
        });




    }
}
