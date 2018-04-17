package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class startQuiz extends AppCompatActivity {

    private Intent fromQuizFragment;
    private String Quiz_id;
    private DatabaseReference QuizRef;
    private DatabaseReference QuizGradeRef;
    private FirebaseAuth mAuth;

    private String numOfQuestions;
    private ArrayList<String> choices;
    private ArrayList<QuestionClass> questionsArray;
    private  int counter;
    private int correctAnswer;
    private int StudentAnswer;
    private Intent refresh;
    private TextView e1,e2,e3,e4,e,id;
    private Button NextQuestion;
    private RadioButton radio1,radio2,radio3,radio4;
    private RadioGroup rg;
    private Map quizInfo;
    private String quizID;
    private String Question,c1,c2,c3,c4,correctAns;
    private int CorrectA;
    private int counter2;
    private String StudentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        fromQuizFragment = getIntent();
        Quiz_id=fromQuizFragment.getStringExtra("quizId");

        refresh=new Intent(this,this.getClass());
        choices = new ArrayList<>();
        questionsArray = new ArrayList<>();

        id=(TextView)findViewById(R.id.QuizID);
        e = (TextView) findViewById(R.id.question);
        e1 = (TextView) findViewById(R.id.choice1);
        e2 = (TextView) findViewById(R.id.choice2);
        e3 = (TextView) findViewById(R.id.choice3);
        e4 = (TextView) findViewById(R.id.choice4);
        radio1=(RadioButton)findViewById(R.id.r1);
        radio2=(RadioButton)findViewById(R.id.r1);
        radio3=(RadioButton)findViewById(R.id.r1);
        radio4=(RadioButton)findViewById(R.id.r1);
        rg=(RadioGroup)findViewById(R.id.choices);
        QuizRef = FirebaseDatabase.getInstance().getReference().child("Quizes");
        QuizGradeRef = FirebaseDatabase.getInstance().getReference().child("QuizGrades");
        mAuth=FirebaseAuth.getInstance();
        StudentId = mAuth.getCurrentUser().getUid();
        NextQuestion = (Button) findViewById(R.id.nextquestion);
        id.setText(Quiz_id);
       QuizGradeRef.child(Quiz_id).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if (dataSnapshot.hasChild(StudentId)) {
                   Toast.makeText(startQuiz.this, "you have already done this quiz", Toast.LENGTH_LONG).show();
                   finish();
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

        QuizRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numOfQuestions=dataSnapshot.child(Quiz_id).child("numOfQuestions").getValue().toString();
                counter = Integer.valueOf(numOfQuestions)-1;
                counter2 = Integer.valueOf(numOfQuestions);
                Log.i("int ref", String.valueOf(counter));

                if (counter == Integer.valueOf(numOfQuestions) - 1) {
                    Fill(counter);
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("int oncreate", String.valueOf(counter));





        NextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( counter2> 0) {
                   counter2--;

                    //getting the correct answer + validating all attributes
                    if(rg.getCheckedRadioButtonId()==-1){
                        StudentAnswer=5;
                    }
                    else{
                        switch (rg.getCheckedRadioButtonId()){
                            case R.id.r1:
                                StudentAnswer=1;
                                break;
                            case R.id.r2:
                                StudentAnswer=2;
                                break;
                            case R.id.r3:
                                StudentAnswer=3;
                                break;
                            case R.id.r4:
                                StudentAnswer=4;
                                break;



                        }
                        Log.d("student", String.valueOf(StudentAnswer));
                    }
                    Log.d("student11", String.valueOf(StudentAnswer));
                    QuestionClass q = new QuestionClass(Question,choices,CorrectA,StudentAnswer);
                    questionsArray.add(q);
                    choices.clear();

                    counter--;

                    Log.i("int next", String.valueOf(counter));
                  if(counter==0)  NextQuestion.setText("Finsh");

                  if(counter>=0) Fill(counter);

                }
                if(counter2==0) {

                    QuizClass quiz = new QuizClass(Quiz_id,questionsArray,Integer.valueOf(numOfQuestions));
                   double result= quiz.QuizResult();

                    String studentEmail = mAuth.getCurrentUser().getEmail();
                    Map grade_email = new HashMap();
                    grade_email.put("grade",String.valueOf(result));
                    grade_email.put("email", studentEmail);

                    QuizGradeRef.child(Quiz_id).child(StudentId).setValue(grade_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               startActivity(new Intent(startQuiz.this,Student_board.class));
                           }
                       }
                   });

                    Log.i("result", String.valueOf(result));





                }


            }
        });
    }

    private void Fill(final int counter) {



        QuizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("int fill", String.valueOf(counter));
               // numOfQuestions = dataSnapshot.child(Quiz_id).child("numOfQuestions").getValue().toString();
                Question = dataSnapshot.child(Quiz_id).child(String.valueOf(counter)).child("question").getValue().toString();
                e.setText(Question);
                c1 = dataSnapshot.child(Quiz_id).child(String.valueOf(counter)).child("choice1").getValue().toString();
                e1.setText(c1);
                c2 = dataSnapshot.child(Quiz_id).child(String.valueOf(counter)).child("choice2").getValue().toString();
                e2.setText(c2);
                c3 = dataSnapshot.child(Quiz_id).child(String.valueOf(counter)).child("choice3").getValue().toString();
                e3.setText(c3);
                c4 = dataSnapshot.child(Quiz_id).child(String.valueOf(counter)).child("choice4").getValue().toString();
                e4.setText(c4);
                correctAns = dataSnapshot.child(Quiz_id).child(String.valueOf(counter)).child("correctAnswer").getValue().toString();
                CorrectA = Integer.valueOf(correctAns);
                choices.add(c1);
                choices.add(c2);
                choices.add(c3);
                choices.add(c4);





            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
