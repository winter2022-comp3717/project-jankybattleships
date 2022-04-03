package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;


public class GameActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        Button submit = findViewById(R.id.confirm_attack);
        Button swap = findViewById(R.id.swap);

        submit.setOnClickListener(effect -> confirmAttackOnClick());
        swap.setOnClickListener(swapBoard -> swapBoardOnClick());
    }

    /**
     * Swap between displaying the player board and the opponent board.
     */
    public void swapBoardOnClick() {
        // Fragment Swapping between your board and your opponents.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        boolean showPlayerBoard = false;

        if(!showPlayerBoard) {
            fragmentTransaction.replace(R.id.main_game_fragment, new GameFragment());
        } else {
            fragmentTransaction.replace(R.id.main_game_fragment, new GameFragment()); // Needs to be changed.
            showPlayerBoard = true;
        }

        fragmentTransaction.commit();
    }

    /**
     * Take the selected guess and add the grid co-ordinates
     */
    public void confirmAttackOnClick() {
        targetEffect();
        updateHostileGrid();
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
    public void updateHostileGrid(String[] guesses) {
        // Needs to write the the list of player guesses to firebase.
        db.collection("sessions").document();
    }

    /**
     * Retrieve updated friendly grid from Firebase.
     */
    public void getFriendlyGrid() {
        // Get the opponents guesses from firebase and run display grid.
    }

    /**
     * Display the given grid to the fragment.
     */
    public void displayGrid(String[] guesses) {
        for (String point: guesses) {
            // Need to get appropriate button from string and pass to target effect.
            targetEffect();
        }
    }

    /**
     * Set the grid color according to presence of a ship.
     */
    public void targetEffect(Button gridPoint) {
    }
}