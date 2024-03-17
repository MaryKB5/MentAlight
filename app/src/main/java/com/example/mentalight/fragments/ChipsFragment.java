package com.example.mentalight.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.mentalight.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChipsFragment extends Fragment {

    private static final String INPUT = "input_texts";

    private String[] inputTexts;
    private ChipGroup chipGroup;
    private View view;

    public ChipsFragment() {
        // Required empty public constructor
    }


    public static ChipsFragment newInstance(String[] inputTexts) {
        ChipsFragment fragment = new ChipsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chipGroup = view.findViewById(R.id.chip_group);

        if (inputTexts != null && chipGroup.getChildCount() == inputTexts.length) {
            for (int i = 0; i < inputTexts.length; i++) {
                String text = inputTexts[i];
                Chip chip = (Chip) chipGroup.getChildAt(i);
                chip.setText(text);
            }
        }
    }


    public boolean oneChipChecked() {
        int checkedChipId = chipGroup.getCheckedChipId();
        return checkedChipId != View.NO_ID;
    }
}