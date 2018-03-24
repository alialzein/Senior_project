package com.example.alialzein.myclassroom.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alialzein.myclassroom.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorWallFragment extends Fragment {


    public InstructorWallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructor_wall, container, false);
    }

}
