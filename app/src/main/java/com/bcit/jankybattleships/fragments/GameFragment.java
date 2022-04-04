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
    private static final String ARG_PLACEMENT_COORDS =
            "com.bcit.jankybattleships.fragments.PLACEMENT_COORDS";
    private static final String ARG_GUESS_COORDS =
            "com.bcit.jankybattleships.fragments.GUESS_COORDS";
    private static final int[][] GRID_IDS = {
            {R.id.grid_1_1, R.id.grid_1_2, R.id.grid_1_3, R.id.grid_1_4, R.id.grid_1_5},
            {R.id.grid_2_1, R.id.grid_2_2, R.id.grid_2_3, R.id.grid_2_4, R.id.grid_2_5},
            {R.id.grid_3_1, R.id.grid_3_2, R.id.grid_3_3, R.id.grid_3_4, R.id.grid_3_5},
            {R.id.grid_4_1, R.id.grid_4_2, R.id.grid_4_3, R.id.grid_4_4, R.id.grid_4_5},
            {R.id.grid_5_1, R.id.grid_5_2, R.id.grid_5_3, R.id.grid_5_4, R.id.grid_5_5}
    };

    private List<String> placementCoords;
    private List<String> guessCoords;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param placementCoords Parameter 1.
     * @param guessCoords Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    public static GameFragment newInstance(ArrayList<String> placementCoords,
                                           ArrayList<String> guessCoords) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PLACEMENT_COORDS, placementCoords);
        args.putStringArrayList(ARG_GUESS_COORDS, guessCoords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placementCoords = getArguments().getStringArrayList(ARG_PLACEMENT_COORDS);
            guessCoords = getArguments().getStringArrayList(ARG_GUESS_COORDS);
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
        if (placementCoords != null) {
            if (guessCoords == null) {
                displayPlacementPreview(view);
            } else {
                displayGameGrid(view);
            }
        }
    }

    /**
     * Sets the appropriate color to each square (button).
     *
     * @param view View object
     */
    public void displayGameGrid(View view) {
        // Write all points to blue.
        for (int[] row : GRID_IDS) {
            for (int point : row) {
                Button gridPoint = view.findViewById(point);
                gridPoint.setBackgroundColor(getResources().getColor(R.color.blue_unknown));
            }
        }
        // Set to teal if a miss set to red if a hit.
        for (String coordinate : guessCoords) {
            int buttonID = GRID_IDS[Integer.parseInt(String.valueOf(coordinate.charAt(0))) - 1]
                    [Integer.parseInt(String.valueOf(coordinate.charAt(2))) - 1];
            Button gridCoordinate = view.findViewById(buttonID);
            for (String placement : placementCoords) {
                if (placement.equals(coordinate)) {
                    gridCoordinate.setBackgroundColor(getResources().getColor(R.color.red_hit));
                    break;
                } else {
                    gridCoordinate.setBackgroundColor(getResources().getColor(R.color.teal_miss));
                }
            }
        }
    }

    public void displayPlacementPreview(View view) {
        // Write all points to blue.
        for (int[] row : GRID_IDS) {
            for (int point : row) {
                Button gridPoint = view.findViewById(point);
                gridPoint.setBackgroundColor(getResources().getColor(R.color.blue_unknown));
            }
        }
        // Set placements to red.
        for (String coordinate : placementCoords) {
            int buttonID = GRID_IDS[Integer.parseInt(String.valueOf(coordinate.charAt(0))) - 1]
                    [Integer.parseInt(String.valueOf(coordinate.charAt(2))) - 1];
            Button gridCoordinate = view.findViewById(buttonID);
            gridCoordinate.setBackgroundColor(getResources().getColor(R.color.red_hit));
        }
    }
}
