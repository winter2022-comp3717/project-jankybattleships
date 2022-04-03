package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShipPlacementActivity extends AppCompatActivity {

    private GameSession session;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_placement);

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
    }
}