package com.example.mentalight.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mentalight.R;

public class FreeTextFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FreeTextFragment() {
        // Leerer öffentlicher Konstruktor, der für Fragmente erforderlich ist
    }

    /**
     * Verwenden Sie diese Factory-Methode, um eine neue Instanz des Fragments zu erstellen
     * mit den bereitgestellten Parametern.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return Eine neue Instanz des FreeTextFragment.
     */
    public static FreeTextFragment newInstance(String param1, String param2) {
        FreeTextFragment fragment = new FreeTextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.free_text, container, false);
    }
}