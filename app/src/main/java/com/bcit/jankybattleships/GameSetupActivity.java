package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.bcit.jankybattleships.data.GameSession;
import com.bcit.jankybattleships.data.Player;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GameSetupActivity extends AppCompatActivity {

    private GameSession session;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        session = (GameSession) extras.getSerializable(MainActivity.EXTRA_NEW_GAME_SESSION);
        player = (Player) extras.getSerializable(MainActivity.EXTRA_PLAYER);
        session.getUpdatedGameScores();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("sessions")
                .document(session.getSessionCode());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    setTitle(document.getId());
                } else {
                    Log.d("INFO", "No document found.");
                }
            } else {
                Log.d("ERROR", "get failed with ", task.getException());
            }
        });

        Button submitPlacement = findViewById(R.id.button_setup_confirm);
        EditText patrolPosition = findViewById(R.id.editText_setup_patrol);
        EditText destroyPosition = findViewById(R.id.editText_setup_destroyer);
        EditText cruisePosition = findViewById(R.id.editText_setup_cruiser);

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