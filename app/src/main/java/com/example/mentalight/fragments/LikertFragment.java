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
import com.google.android.material.radiobutton.MaterialRadioButton;


public class LikertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INPUT = "input_texts";

    private int radiobuttonId = 1;

    private String[] inputTexts;
    private RadioGroup radioGroup;
    private View view;

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
        view = inflater.inflate(R.layout.likert_scale, container, false);

        radioGroup = view.findViewById(R.id.radioGroup);

        if (inputTexts != null) {
            radiobuttonId = 1;
            for (String text : inputTexts) {
                RadioButton radioButton = new RadioButton(requireContext());
                radioButton.setText(text);
                radioButton.setTextColor(getResources().getColor(R.color.dark_blue));
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);

                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.topMargin = dpToPx(20);
                radioButton.setId(radiobuttonId);
                radiobuttonId++;
                radioGroup.addView(radioButton, params);
            }
        }
        return view;
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public String getCheckedRadioButtonText() {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = view.findViewById(selectedRadioButtonId);
            String selectedRadioButtonText = selectedRadioButton.getText().toString();

            return selectedRadioButtonText;
        } else {
            return "nichts ausgewählt!";
        }
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