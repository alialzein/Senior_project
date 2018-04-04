package com.example.alialzein.myclassroom;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorQuizFragment extends Fragment {
    private View myView;
    private Button b;


    public InstructorQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myView= inflater.inflate(R.layout.fragment_instructor_quiz, container, false);
        b = (Button) myView.findViewById(R.id.create);
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



        return myView;
    }


}
