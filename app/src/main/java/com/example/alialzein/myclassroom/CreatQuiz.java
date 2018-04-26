package com.example.alialzein.myclassroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatQuiz extends AppCompatActivity {
   private QuestionClass q;
    private QuizClass quiz;
   private ArrayList<QuestionClass> questions = new ArrayList<QuestionClass>(4);
   private int numOfQuestions;
   private ArrayList<String> choices;
  private  int counter;
   private int correctAnswer;
   private Intent tooQuiz,refresh;
   private EditText e1,e2,e3,e4,e,id;
   private Button Next;
   private RadioButton radio1,radio2,radio3,radio4;
   private RadioGroup rg;
    private DatabaseReference QuizRef;
    private Map quizInfo;
    private String quizID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_quiz);
        tooQuiz=new Intent(this,Instructor_board.class);
        refresh=new Intent(this,this.getClass());
        QuizRef = FirebaseDatabase.getInstance().getReference().child("Quizes");
        choices = new ArrayList<>();
        id=(EditText)findViewById(R.id.QuizID);
        e = (EditText) findViewById(R.id.question);
        e1 = (EditText) findViewById(R.id.choice1);
        e2 = (EditText) findViewById(R.id.choice2);
        e3 = (EditText) findViewById(R.id.choice3);
        e4 = (EditText) findViewById(R.id.choice4);
        radio1=(RadioButton)findViewById(R.id.r1);
        radio2=(RadioButton)findViewById(R.id.r1);
        radio3=(RadioButton)findViewById(R.id.r1);
        radio4=(RadioButton)findViewById(R.id.r1);
        rg=(RadioGroup)findViewById(R.id.choices);
        quizInfo = new HashMap();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the number of questions in this quiz : ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // String pin_entered = input.getText().toString();
                if(TextUtils.isEmpty(input.getText())||input.getText().toString().contains(" ")){
                    showMessage("Empty field","Please Make Sure To Enter The Number Of Questions Your Quiz Consists of");
                    startActivity(refresh);
                }
                else{
                  numOfQuestions=Integer.parseInt(input.getText().toString());
                  counter=numOfQuestions;
                }
            }

        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(tooQuiz);
                finish();
            }

        });

        builder.show();

        Next = (Button) findViewById(R.id.next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             if(counter >=0) {
                 //getting the question
                    String question = e.getText().toString();
                 quizInfo.put("question", question);
                    //getting the 4 choices
                 //   choices = new String[]{e1.getText().toString(), e2.getText().toString(), e3.getText().toString(), e4.getText().toString()};
                 choices.add(e1.getText().toString());
                 choices.add(e2.getText().toString());
                 choices.add(e3.getText().toString());
                 choices.add(e4.getText().toString());
                 quizInfo.put("choice1", e1.getText().toString());
                 quizInfo.put("choice2", e2.getText().toString());
                 quizInfo.put("choice3", e3.getText().toString());
                 quizInfo.put("choice4", e4.getText().toString());

                    //getting the correct answer + validating all attributes
                    if(rg.getCheckedRadioButtonId()==-1||e1.getText().toString().matches("")||e2.getText().toString().matches("")
                            ||e3.getText().toString().matches("")||e4.getText().toString().matches("")
                            ||e.getText().toString().matches("")||id.getText().toString().matches("")){
                        showMessage("Missing","you have to choose the correct answer , and fill all the information needed");

                    }
                    else{
                        switch (rg.getCheckedRadioButtonId()){
                            case R.id.r1:
                                correctAnswer=1;
                                Toast.makeText(getApplicationContext(), "correct choice selected "+correctAnswer, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.r2:
                                correctAnswer=2;
                                Toast.makeText(getApplicationContext(), "correct choice selected "+correctAnswer, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.r3:
                                correctAnswer=3;
                                Toast.makeText(getApplicationContext(), "correct choice selected "+correctAnswer, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.r4:
                                correctAnswer=4;
                                Toast.makeText(getApplicationContext(), "correct choice selected "+correctAnswer, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        quizInfo.put("correctAnswer", correctAnswer);
                        //creating a question object to be added later to a quiz object then into the DB
                        q = new QuestionClass(question, choices, correctAnswer);
                        questions.add(q);
                        e1.setText("");
                        e2.setText("");
                        e3.setText("");
                        e4.setText("");
                        e.setText("");
                        choices.clear();
                        counter--;
                        showMessage("Questions Left",counter+" question(s) left");

                    }
                    quizID=id.getText().toString();
                 QuizRef.child(quizID).child("numOfQuestions").setValue(numOfQuestions);
                 QuizRef.child(quizID).child(String.valueOf(counter)).setValue(quizInfo);



                }
             if(counter == 0){
                 //String question = e.getText().toString();
                 //choices = new String[]{e1.getText().toString(), e2.getText().toString(), e3.getText().toString(), e4.getText().toString()};
                 //q = new QuestionClass(question, choices, correctAnswer);
                 //questions.add(q);
                    showMessage("DONE","the quiz has been created");

                   // quiz=new QuizClass(quizID,questions,numOfQuestions);
                    //QUIZ OBJECT READY TO INSERT TO DB
               //  QuizRef.child(quizID).setValue(quiz);
                    startActivity(tooQuiz);
                    finish();


               }

            }//

        });



    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        //builder.setCancelable(false);
        builder.show();
    }
}
