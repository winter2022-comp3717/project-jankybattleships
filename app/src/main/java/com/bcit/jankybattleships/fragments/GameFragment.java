package com.bcit.jankybattleships.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bcit.jankybattleships.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COORDINATES = "com.bcit.jankybattleships.fragments.COORDINATES";
//    private static final String ARG_PARAM2 = "param2";

    private List<String> coordinates;
//    private String mParam2;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param coords Parameter 1.
     * @return A new instance of fragment GameFragment.
     */
    public static GameFragment newInstance(ArrayList<String> coords) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_COORDINATES, coords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coordinates = getArguments().getStringArrayList(ARG_COORDINATES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: display received coordinates on board
    }
}