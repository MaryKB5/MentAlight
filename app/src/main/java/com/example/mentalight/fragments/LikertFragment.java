package com.example.mentalight.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mentalight.R;
import com.google.android.material.radiobutton.MaterialRadioButton;


public class LikertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INPUT = "input_texts";

    private String[] inputTexts;

    public LikertFragment() {
        // Required empty public constructor
    }


    public static LikertFragment newInstance(String[] inputTexts) {
        LikertFragment fragment = new LikertFragment();
        Bundle args = new Bundle();
        args.putStringArray(INPUT, inputTexts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputTexts = getArguments().getStringArray(INPUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.likert_scale, container, false);
        int j = 1;

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        Log.d("isterda", "ja");
        if (view != null){
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                View childView = radioGroup.getChildAt(i);
                if (childView instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) childView;
                    radioButton.setText(inputTexts[i]);
                }
            }
        }
        return view;
    }
}