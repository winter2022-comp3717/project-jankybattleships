package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
    }

    /**
     * Display the given grid to the fragment.
     */
    public void displayGrid(View view) {
    }

    /**
     * Swap fragment view between your grid and your opponents.
     */
    public void swapView(View view) {
    }

    /**
     * Set the button color according to presence of a ship.
     * @param view:
     */
    public void targetEffect(View view) {
    }
}