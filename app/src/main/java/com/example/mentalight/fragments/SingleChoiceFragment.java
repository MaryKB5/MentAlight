package com.example.mentalight.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mentalight.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleChoiceFragment extends Fragment {

    private static final String INPUT = "input_texts";
    private String[] inputTexts;
    private RadioGroup radioGroup;
    private View view;


    public SingleChoiceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SingleChoiceFragment newInstance(String[] inputTexts) {
        SingleChoiceFragment fragment = new SingleChoiceFragment();
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


        view = inflater.inflate(R.layout.single_choice, container, false);
        radioGroup = view.findViewById(R.id.radioGroup);

        if (inputTexts != null) {
            for (int i = 0; i < 2; i++) {
                String text = inputTexts[i];
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setText(text);
            }
        }
        return view;
    }


    public boolean oneRadioButtonChecked() {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId != -1) {
            return true;
        } else {
            return false;
        }
    }


}