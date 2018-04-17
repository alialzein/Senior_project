package com.example.alialzein.myclassroom;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorQuizFragment extends Fragment {
    private View myView;
    private Button b;
    private RecyclerView allGradesRecyclerView;
    private Button getGrades;
    private EditText idQuizGrades;
    private String idGrades;
    private DatabaseReference QuizGradesRef;


    public InstructorQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myView= inflater.inflate(R.layout.fragment_instructor_quiz, container, false);
        b = (Button) myView.findViewById(R.id.create);
        allGradesRecyclerView = (RecyclerView) myView.findViewById(R.id.allGrades_recyclerView);
        allGradesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getGrades = (Button) myView.findViewById(R.id.get_grades_inst);
        idQuizGrades = (EditText) myView.findViewById(R.id.id_quiz_grades);



        try {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),CreatQuiz.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        getGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idGrades = idQuizGrades.getText().toString();
                if (TextUtils.isEmpty(idGrades)) {
                    Toast.makeText(getActivity(), "please enter the id of the quiz to get the grades", Toast.LENGTH_SHORT).show();
                } else {

                     FirebaseDatabase.getInstance().getReference().child("QuizGrades").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(idGrades)) {
                                QuizGradesRef = FirebaseDatabase.getInstance().getReference().child("QuizGrades").child(idGrades);


                                FirebaseRecyclerAdapter<allGrades,GradesViewHolder> firebaseRecyclerAdapter=
                                        new FirebaseRecyclerAdapter<allGrades, GradesViewHolder>
                                                (allGrades.class,
                                                        R.layout.all_grades_display,
                                                        GradesViewHolder.class,
                                                        QuizGradesRef




                                                )


                                        {
                                            @Override
                                            protected void populateViewHolder(GradesViewHolder viewHolder, allGrades model, int position) {

                                                viewHolder.setEmail(model.getEmail());
                                                viewHolder.setGrade(model.getGrade());
                                                String studentId = getRef(position).getKey();

                                            }
                                        };

                                allGradesRecyclerView.setAdapter(firebaseRecyclerAdapter);

                            } else {
                                Toast.makeText(getActivity(), "This quiz does not exist", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });








                }
            }
        });



        return myView;
    }

    public static class GradesViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public GradesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setEmail(String email) {
            TextView std_email = (TextView) mView.findViewById(R.id.std_email);
            std_email.setText(email);

        }
        public void setGrade(String grade) {
            TextView std_garde = (TextView) mView.findViewById(R.id.std_grade);
            std_garde.setText(grade);

        }
    }





}
