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
// Ein Fragment für die Auswahl einer einzelnen Option
public class SingleChoiceFragment extends Fragment {

    private static final String INPUT = "input_texts";
    private String[] inputTexts;
    private RadioGroup radioGroup;
    private View view;


    public SingleChoiceFragment() {
        // Leerer öffentlicher Konstruktor, der für Fragmente erforderlich ist
    }

    // Methode zum Erstellen einer neuen Instanz von SingleChoiceFragment mit übergebenen Eingabetexten
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

    // Methode zur Überprüfung, ob mindestens ein RadioButton ausgewählt ist
    public boolean oneRadioButtonChecked() {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId != -1) {
            return true;
        } else {
            return false;
        }
    }


}