package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.bcit.jankybattleships.data.GameSession;
import com.bcit.jankybattleships.data.GameStatus;
import com.bcit.jankybattleships.data.Player;
import com.bcit.jankybattleships.fragments.GameFragment;
import com.bcit.jankybattleships.fragments.LeaderboardFragment;
import com.bcit.jankybattleships.fragments.MenuFragment;
import com.bcit.jankybattleships.fragments.OptionsFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GameSetupActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private GameSession session;
    private Player player;
    private List<String> coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        session = (GameSession) extras.getSerializable(GameSession.EXTRA_NEW_GAME_SESSION);
        player = (Player) extras.getSerializable(Player.EXTRA_PLAYER);
        session.getUpdatedGameScores();

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

        Button submitButton = findViewById(R.id.button_setup_confirm);
        Button previewButton = findViewById(R.id.button_setup_preview);
        EditText patrolEditText = findViewById(R.id.editText_setup_patrol);
        EditText destroyerEditText = findViewById(R.id.editText_setup_destroyer);
        EditText battleshipEditText = findViewById(R.id.editText_setup_battleship);

        submitButton.setOnClickListener(view -> submitPositions());
        previewButton.setOnClickListener(view -> previewPositions());
    }

    /**
     *
     */
    @SuppressLint("DefaultLocale")
    public void submitPositions() {
        /*
        Take the inputs from the text boxes, convert them to parts a grid and submit to Firebase.
         */
        populateCoordsList();
        if (player.getPlayerNum() == 1) {
            session.updateGameStatus(GameStatus.PLACEMENT_WAIT_FOR_P2);
        } else {
            session.updateGameStatus(GameStatus.PLACEMENT_WAIT_FOR_P1);
        }
        DocumentReference df = db.collection("sessions")
                .document(session.getSessionCode());

        df.update(String.format("p%d_coordinates", player.getPlayerNum()), coordinates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("INFO", "Game status successfully updated!");
                    startGameOnPlacementEnd();
                })
                .addOnFailureListener(e -> Log.w("ERROR", "Error updating status", e));
    }

    private void startGameOnPlacementEnd() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                session.getUpdatedGameStatus();
                // check if other player is already done placement and is waiting
                if ((player.getPlayerNum() == 1 && session.getStatus() == GameStatus.PLACEMENT_WAIT_FOR_P1)
                        || (player.getPlayerNum() == 2 && session.getStatus() == GameStatus.PLACEMENT_WAIT_FOR_P2)) {
                    session.updateGameStatus(GameStatus.P1_TURN);
                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable(GameSession.EXTRA_NEW_GAME_SESSION, session);
                    extras.putSerializable(Player.EXTRA_PLAYER, player);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    handler.postDelayed(this, GameSession.REFRESH_DELAY);
                }
            }
        };
        handler.postDelayed(runnable, GameSession.REFRESH_DELAY);
    }

    public void previewPositions() {
        populateCoordsList();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView_setup,
                GameFragment.newInstance((ArrayList<String>) coordinates));
        fragmentTransaction.commit();
    }

    private void populateCoordsList() {
        EditText patrolEditText = findViewById(R.id.editText_setup_patrol);
        EditText destroyerEditText = findViewById(R.id.editText_setup_destroyer);
        EditText battleshipEditText = findViewById(R.id.editText_setup_battleship);
        coordinates = new ArrayList<>();
        coordinates.addAll(getCoordinatesFromText(patrolEditText.getText().toString()));
        coordinates.addAll(getCoordinatesFromText(destroyerEditText.getText().toString()));
        coordinates.addAll(getCoordinatesFromText(battleshipEditText.getText().toString()));
    }

    private List<String> getCoordinatesFromText(String text) {
        List<String> coords = new ArrayList<>();
        String[] splitText = text.split(", ");
        for (String str : splitText) {
            // adds "X,Y" (from "(X,Y)") to the list
            coords.add(str.replaceAll("[()]", ""));
        }
        return coords;
    }

    /**
     * Helper to convert string coordinates into 2D array co-ordinates.
     * @param input a String in format (X,X X,X)
     */
    public String[] placementHelper(String input) {
        return input.split(" ");
    }
}