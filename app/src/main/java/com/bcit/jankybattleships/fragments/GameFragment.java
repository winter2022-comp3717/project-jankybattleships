package com.bcit.jankybattleships.fragments;

import android.graphics.Color;
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

    /**
     * Write the appropriate color to each android button.
     *
     * @param view View object
     * @param guesses String array containing the guessed co-ordinates.
     * @param boatPlacements String array containing the boat locations.
     */
    public void printGrid(View view, String[] guesses, String[] boatPlacements) {
        int[][] gridIDs = {{R.id.grid_1_1, R.id.grid_1_2, R.id.grid_1_3, R.id.grid_1_4, R.id.grid_1_5},
                {R.id.grid_2_1, R.id.grid_2_2, R.id.grid_2_3, R.id.grid_2_4, R.id.grid_2_5},
                {R.id.grid_3_1, R.id.grid_3_2, R.id.grid_2_3, R.id.grid_2_4, R.id.grid_2_5},
                {R.id.grid_4_1, R.id.grid_4_2, R.id.grid_4_3, R.id.grid_4_4, R.id.grid_4_5},
                {R.id.grid_5_1, R.id.grid_5_2, R.id.grid_5_3, R.id.grid_5_4, R.id.grid_5_5}};

        // Write all points to blue.
        for (int[] row : gridIDs) {
            for (int point : row) {
                Button gridPoint = view.findViewById(point);
                gridPoint.setBackgroundColor(getResources().getColor(R.color.blue_unknown));
            }
        }

        // Set to teal if a miss set to red if a hit.
        for (String coordinate : guesses) {
            int buttonID = gridIDs[coordinate.charAt(0)][coordinate.charAt(2)];
            Button gridCoordinate = view.findViewById(buttonID);
            for (String placement : boatPlacements) {
                if (placement.equals(coordinate)) {
                    gridCoordinate.setBackgroundColor(getResources().getColor(R.color.red_hit));
                    break;
                } else {
                    gridCoordinate.setBackgroundColor(getResources().getColor(R.color.teal_miss));
                }
            }
        }
    }
}