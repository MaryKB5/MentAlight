package com.example.mentalight.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.mentalight.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckboxFragment extends Fragment {

    private static final String ARG_CHECKBOX_TEXTS = "checkbox_texts";

    private String[] checkBoxTexts;
    public CheckboxFragment() {
        // Required empty public constructor
    }

    // Methode zum Erstellen einer neuen Instanz von CheckboxFragment mit übergebenen CheckBox-Texten
    public static CheckboxFragment newInstance(String[] checkBoxTexts) {
        CheckboxFragment fragment = new CheckboxFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_CHECKBOX_TEXTS, checkBoxTexts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            checkBoxTexts = getArguments().getStringArray(ARG_CHECKBOX_TEXTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkbox, container, false);

        if (checkBoxTexts != null) {
            LinearLayout checkBoxContainer = view.findViewById(R.id.checkbox_container);
            for (String text : checkBoxTexts) {
                CheckBox checkBox = new CheckBox(requireContext());
                checkBox.setText(text);
                checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                checkBoxContainer.addView(checkBox);
            }
        }

        return view;
    }
}