package com.example.alialzein.myclassroom;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentQuizFragment extends Fragment {

    private DatabaseReference QuizRef;
    private View myView;
    private Button startQuiz,getQuizGrade;
    private EditText quizId, quizIdGrade;
    private TextView quiz_grade_textView;
    // private int counter;
    // private String numOfQuestion;
    //  private Button nextQuestion;
    private DatabaseReference QuizGradeRef;
    private FirebaseAuth mAuth;
    private String StudentId;

    public StudentQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      myView=inflater.inflate(R.layout.fragment_student_quiz, container, false);

        startQuiz = (Button) myView.findViewById(R.id.startQuiz);
        quizId = (EditText) myView.findViewById(R.id.quizId);
        getQuizGrade = (Button) myView.findViewById(R.id.getQuizGrade);
        quizIdGrade = (EditText) myView.findViewById(R.id.quiz_grade_id);
        quiz_grade_textView = (TextView) myView.findViewById(R.id.quiz_grade_textView);



        mAuth=FirebaseAuth.getInstance();
        StudentId = mAuth.getCurrentUser().getUid();

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizRef = FirebaseDatabase.getInstance().getReference().child("Quizes");
                QuizRef.addValueEventListener(new ValueEventListener() {
                    final String Id=quizId.getText().toString();
                    boolean empty =quizId.getText().toString().isEmpty();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(Id)&& !empty) {
                            Intent toStartQuiz = new Intent(getActivity(), startQuiz.class);
                            toStartQuiz.putExtra("quizId", quizId.getText().toString());
                            quizId.setText("");
                            startActivity(toStartQuiz);

                        }
                        else{
                            Toast.makeText(getActivity(), "This quiz does not exist...", Toast.LENGTH_SHORT).show();
                        }

                    if(empty){
                        Toast.makeText(getActivity(), "please make sure to enter the quiz ID", Toast.LENGTH_SHORT).show();
                    }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });





        getQuizGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String gradeId = quizIdGrade.getText().toString();
                QuizGradeRef= FirebaseDatabase.getInstance().getReference().child("QuizGrades");
                QuizGradeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(gradeId)) {
                            if (dataSnapshot.child(gradeId).hasChild(StudentId)) {
                                String grade = dataSnapshot.child(gradeId).child(StudentId).child("grade").getValue().toString();
                               // Toast.makeText(getActivity(), grade, Toast.LENGTH_SHORT).show();
                                quiz_grade_textView.setText("Your grade is: "+grade);
                            }else{
                                Toast.makeText(getActivity(), "you have not done this quiz yet", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(getActivity(), "This quiz does not exist...", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });




        return myView;
    }

}
