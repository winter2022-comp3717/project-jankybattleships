package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bcit.jankybattleships.fragments.GameFragment;
import com.google.firebase.firestore.FirebaseFirestore;


public class GameActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        setContentView(R.layout.activity_game);


        TextView name1 = findViewById(R.id.textView_game);
        TextView name2 = findViewById(R.id.textView_game_2);
        if (MainActivity.DARK_MODE) {
            getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            name1.setTextColor(Color.WHITE);
            name2.setTextColor(Color.WHITE);
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            name1.setTextColor(Color.BLACK);
            name2.setTextColor(Color.BLACK);
        }

        Button submit = findViewById(R.id.button_game_confirm);
        Button swap = findViewById(R.id.button_game_swap);

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
            fragmentTransaction.replace(R.id.fragmentContainerView_game, new GameFragment());
        } else {
            fragmentTransaction.replace(R.id.fragmentContainerView_game, new GameFragment());
            showPlayerBoard = true;
        }

        fragmentTransaction.commit();
    }

    /**
     * Take the selected guess and add the grid co-ordinates
     */
    public void confirmAttackOnClick() {
        targetEffect(null); // TODO: fix null
        updateHostileGrid(null); // TODO: fix null
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
            targetEffect(null); // TODO: fix null
        }
    }

    /**
     * Set the grid color according to presence of a ship.
     */
    public void targetEffect(Button gridPoint) {
    }
}