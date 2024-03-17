package com.example.mentalight.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mentalight.OnQuestionnaireClickedListener;
import com.example.mentalight.OnStartButtonClickListener;
import com.example.mentalight.Questionnaire;
import com.example.mentalight.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {

    private static final String ARG_TITLES_ARRAY = "titles_array";

    private String[] titlesArray;
    private View view;
    private OnQuestionnaireClickedListener questionnaireClickedListener;

    public OverviewFragment() {
        // Required empty public constructor
    }
    public static OverviewFragment newInstance(String[] titlesArray) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_TITLES_ARRAY, titlesArray);
        fragment.setArguments(args);
        return fragment;
    }

    public void setQuestionnaireClickedListener(OnQuestionnaireClickedListener listener) {
        this.questionnaireClickedListener= listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titlesArray = getArguments().getStringArray(ARG_TITLES_ARRAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.questionnaire_overview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.dark_blue));
        if (titlesArray != null) {
            createButtonsForTitles(titlesArray);
        }
    }

    private void createButtonsForTitles(String[] titlesArray) {
        LinearLayout buttonContainer = view.findViewById(R.id.questionnaire_container);
        for (String title : titlesArray) {
            Button button = new Button(requireContext());
            button.setText(title);
            button.setTextSize(30);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (questionnaireClickedListener != null) {
                        questionnaireClickedListener.onQuestionnaireClicked(title);
                    }
                }
            });
            buttonContainer.addView(button);
        }
    }
}



