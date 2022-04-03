package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bcit.jankybattleships.R;
import com.google.firebase.firestore.FirebaseFirestore;


public class GameActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    /**
     * Retrieve the opponents grid array from Firebase.
     */
    public void getHostileGrid(String roomCode) {
        // Get the grid
        db.collection("sessions").document(roomCode).get();
    }

    /**
     * Send updated opponent grid back to Firebase.
     */
    public void updateHostileGrid() {

    }

    /**
     * Retrieve updated friendly grid from Firebase.
     */
    public void getFriendlyGrid() {

    }

    /**
     * Display the given grid to the fragment.
     */
    public void displayGrid() {

    }

    /**
     * Swap fragment view between your grid and your opponents when switch view is pressed.
     */
    public void swapView() {

    }

    /**
     * Set the grid color according to presence of a ship.
     */
    public void targetEffect() {

    }

    /**
     * Submit the upgraded grid
     */
    public void submitMove() {
        // Button submit = findViewById() TODO: Submit button ID here
        targetEffect();
        updateHostileGrid();
    }
}