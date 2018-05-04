package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class instructor_quiz_statistics extends AppCompatActivity {
    private Intent fromQuizFragment;
    private ProgressBar p;
    private TextView e,proposal,missed,answeredright,answeredwrong;
    private Button reminder;
    private String Quiz_id;
    private DatabaseReference QuizRef;
    private DatabaseReference QuizGradeRef;
    private DatabaseReference QuizStudentRef;
    private DatabaseReference Instructor_User , studentsRef;
    private FirebaseAuth mAuth;
    private String InstructorId;
    private String InstructorEmail;
    private String Instructname;
    private String StudentId;
    private String studentEmail;
    private String StudentName;
    private List<String> studentsID;
    private int numofquestions;
    private int enrolled;
    private int numberofallstudents;
    private int[]flags,flags1;
    private ArrayList<String> correctAnswers = new ArrayList<String>();
    private ArrayList<stu_quiz_stats> students = new ArrayList<stu_quiz_stats>();
    private List<String> studentAnswers=new ArrayList<String>();
    private List<String>IDs=new ArrayList<String>();
    private ArrayList<Double> grades =new ArrayList<Double>();
    private ArrayList<String> questions=new ArrayList<String>();
    private ArrayList<ArrayList<String>> B=new ArrayList<ArrayList<String>>();
    private Double highestGrade;
    private int highestGradeIndex;
    private int index ,max ,index1 , max1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Quiz Statistics");
        setSupportActionBar(toolbar);

        //declarations
        p=(ProgressBar)findViewById(R.id.progress);
        e= (TextView) findViewById(R.id.percantage_p);
        proposal=(TextView) findViewById(R.id.emailproposal);
        answeredright=(TextView)findViewById(R.id.mostansweredcorrect);
        answeredwrong=(TextView)findViewById(R.id.answeredw);
        missed=(TextView) findViewById(R.id.numofmissed);
        reminder=(Button)findViewById(R.id.reminderstu);
        fromQuizFragment=getIntent();
        Quiz_id=fromQuizFragment.getStringExtra("quizid");
        toolbar.setTitle("Quiz : "+Quiz_id);
        studentsRef=FirebaseDatabase.getInstance().getReference().child("students");
        QuizRef = FirebaseDatabase.getInstance().getReference().child("Quizes");
        QuizGradeRef = FirebaseDatabase.getInstance().getReference().child("QuizGrades");
        QuizStudentRef = FirebaseDatabase.getInstance().getReference().child("QuizStudent");
        Instructor_User= FirebaseDatabase.getInstance().getReference().child("users");
        mAuth=FirebaseAuth.getInstance();
        InstructorId=mAuth.getCurrentUser().getUid();
                    //getting the number of students in the classroom
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberofallstudents=(int) (dataSnapshot.getChildrenCount());
                Log.i("number of ALL",String.valueOf(numberofallstudents));
                QuizStudentRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                     enrolled=(int) (dataSnapshot.child(Quiz_id).getChildrenCount());
                        Log.i("number of enrolled",String.valueOf(enrolled));
                        e.setText(perecnatge(numberofallstudents,enrolled)+"%");
                        p.setProgress(perecnatge(numberofallstudents,enrolled));
                        missed.setText((numberofallstudents-enrolled)+" Student(s) Remained");
                        if(perecnatge(numberofallstudents,enrolled)==100){
                            missed.setText("All Students Done The Quiz!");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


                    //getting the highest Grade

        QuizGradeRef.child(Quiz_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot result) {
                //adding all student IDs that enrolled this quiz
                int counter = (int) result.getChildrenCount();
                for(DataSnapshot dsp : result.getChildren()){
                    IDs.add(dsp.getKey());

                }
                for(String data:IDs){
                    Toast.makeText(instructor_quiz_statistics.this,data,Toast.LENGTH_LONG).show();
                    grades.add(Double.parseDouble(result.child(data).child("grade").getValue().toString()));
                    String G =result.child(data).child("grade").getValue().toString();
                    Log.i("IDDDD",G);
                    Log.i("IDDDD",data);
                }
              //adding all grades
          /* for(int i =0 ; i<IDs.size();i++){

               grades.add(Double.parseDouble(result.child(IDs.get(i)).child("grade").getValue().toString()));
           }b*/
           //getting highest grade
           for(int i=0;i<grades.size();i++){
               highestGrade=-1.0;
               highestGradeIndex=-1;
               if(grades.get(i)>highestGrade){
                   highestGrade=grades.get(i);
                   highestGradeIndex=i;
               }
           }
           Log.i("indexh",String.valueOf(highestGradeIndex));
           StudentId=IDs.get(highestGradeIndex);
                studentsRef.child(StudentId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        studentEmail=dataSnapshot.child("email").getValue().toString();
                        StudentName=dataSnapshot.child("name").getValue().toString();
                        proposal.setText("Do You Want To Congratulate Mr/Ms "+StudentName+" For Getting The Highest Grade In This Quiz ?");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


                   //getting the info about this quiz (number of questions + correct answer + questions )
        QuizRef.child(Quiz_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numofquestions=Integer.parseInt(dataSnapshot.child("numOfQuestions").getValue().toString());
                flags=new int[numofquestions];
                flags1=new int[numofquestions];
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                     if(dsp.hasChild("correctAnswer")){
                         correctAnswers.add(dsp.child("correctAnswer").getValue().toString());
                         String y=dsp.child("correctAnswer").getValue().toString();
                         questions.add(dsp.child("question").getValue().toString());
                         String x=dsp.child("question").getValue().toString();
                     }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //most answered right and wrong
        QuizStudentRef.child(Quiz_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot result) {

                studentsID = new ArrayList<String>(); // Result will be holded Here
                for(DataSnapshot dsp : result.getChildren()){
                    studentsID.add(String.valueOf(dsp.getKey())); //add result into array list
                     String id =String.valueOf(dsp.getKey());
                    int counter=numofquestions;
                   for (DataSnapshot dsp1 : result.child(id).getChildren()){
                         studentAnswers.add(dsp1.getValue().toString());
                         Log.i("numofqqq", String.valueOf(counter));
                        }

                    for(int i=0;i<correctAnswers.size();i++){
                        if(correctAnswers.get(i).equals(String.valueOf(studentAnswers.get(i).charAt(16)))){
                            flags[i]++;
                            Log.i("may", String.valueOf(flags[i]));
                        }else{
                            flags1[i]++;
                        }

                    }
                    studentAnswers.clear();

                }

                index =-1;
                max=-1;
                for (int i=0;i<flags.length;i++){
                    if(flags[i]>max){
                        max=flags[i];
                        index=i;
                    }
                }
                index1=-1;
                max1=-1;
                for (int i=0;i<flags1.length;i++){
                    if(flags1[i]>max1){
                        max1=flags1[i];
                        index1=i;
                    }
                }
                answeredright.setText("The most Answered Question is : "+questions.get(index)+" with : "+max+" times answered correctly");
                answeredwrong.setText("The most Answered Question is : "+questions.get(index1)+" with : "+max1+" times answered Wrong");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //getting the instructor's email
        Instructor_User.child(InstructorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InstructorEmail=dataSnapshot.child("email").getValue().toString();
                Instructname=dataSnapshot.child("name").getValue().toString();
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });//instructor's email done

        //generating EMAIL AT the float button

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    //sending email
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{studentEmail});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Congratualtions !");
                    intent.putExtra(Intent.EXTRA_TEXT, "Dear "+StudentName+"\n"+"I wanted To Congradulate You For Getting the Highest Grade In Quiz : "+Quiz_id+
                            " By Scoring : "+highestGrade+"%"+"\n"+"\n"+"\n"+"Best Regards !"+"\n"+" PHD.  "+Instructname+
                            "\n"+"\n"+"\n"+"Edited By MyClassroom team @2018"+"\n"+"Thank You For Using Our Application !");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(Intent.createChooser(intent, "Send email..."),12);
                }catch (Exception e){
                    Toast.makeText(instructor_quiz_statistics.this,"Unable to open Email Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });



        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(instructor_quiz_statistics.this,Instructor_board.class));
            }
        });



    }
    public int perecnatge (int all , int enrolled){
        double answer = (100*enrolled)/all;
        return (int)answer ;
    }

}
