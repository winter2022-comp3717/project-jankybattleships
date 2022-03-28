package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GameSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup2);
    }

    /**
     *
     */
    public void submitPositions() {
        /*
        Take the inputs from the text boxes, convert them to parts a grid and submit to Firebase.
         */
    }
}