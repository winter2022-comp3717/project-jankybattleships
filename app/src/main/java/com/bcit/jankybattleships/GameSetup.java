package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Array;

public class GameSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup2);

        Button submitPlacement = findViewById(R.id.confirm_placement);
        EditText patrolPosition = findViewById(R.id.patrol_pos_input);
        EditText destroyPosition = findViewById(R.id.destroyer_pos_input);
        EditText cruisePosition = findViewById(R.id.cruiser_pos_input);

        String patrolPositionString = patrolPosition.getText().toString();
        String destroyPositionString = destroyPosition.getText().toString();
        String cruisePositionString = cruisePosition.getText().toString();



        submitPlacement.setOnClickListener(view -> submitPositions());
    }

    /**
     *
     */
    public void submitPositions() {
        /*
        Take the inputs from the text boxes, convert them to parts a grid and submit to Firebase.
         */
    }

    /**
     * Helper to convert string coordinates into 2D array co-ordinates.
     * @param input a String in format (X,X X,X)
     */
    public String[] placementHelper(String input) {
        return input.split(" ");
    }
}