package com.bcit.jankybattleships.data;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameSession implements Serializable {

    public static final String EXTRA_NEW_GAME_SESSION =
            "com.bcit.jankybattleships.data.NEW_GAME_SESSION";
    public static final int REFRESH_DELAY = 5000;

    private String sessionCode = null;
    private GameStatus status = null;
    private Map<String, Long> scores = new HashMap<>();

    public GameSession() {}

    public void createGameSessionInDb() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> room = new HashMap<>();
        room.put("status", status);

        Map<String, Object> scores = new HashMap<>();
        scores.put(getUserIdOrAnonString(1), 0);
        room.put("scores", scores);

        db.collection("sessions").document(sessionCode)
                .set(room)
                .addOnSuccessListener(aVoid -> Log.d("INFO", "New game session added."))
                .addOnFailureListener(e -> Log.w("ERROR", "Error adding document", e));
    }

    public void updateGameStatus(GameStatus newStatus) {
        status = GameStatus.SHIP_PLACEMENT;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference df = db.collection("sessions").document(sessionCode);
        df.update("status", newStatus)
                .addOnSuccessListener(aVoid -> Log.d("INFO",
                        "Game status successfully updated!"))
                .addOnFailureListener(e -> Log.w("ERROR", "Error updating status", e));
    }

    public void updateGameScoreForPlayer(int playerNumber, int newScore) {
        scores.replace(getUserIdOrAnonString(playerNumber), (long) newScore);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference df = db.collection("sessions").document(sessionCode);

        df.update(String.format("scores.%s", getUserIdOrAnonString(playerNumber)), newScore)
                .addOnSuccessListener(aVoid -> Log.d("INFO",
                        "Game scores successfully updated!"))
                .addOnFailureListener(e -> Log.w("ERROR", "Error updating scores", e));
    }

    @SuppressLint("DefaultLocale")
    public String getUserIdOrAnonString(int playerNum) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return String.format("Anon%s%d", sessionCode, playerNum);
        }
    }

    public void getUpdatedGameStatus() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("sessions").document(sessionCode);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (!status.getStatus().equals(document.get("status"))) {
                        status = GameStatus.valueOf((String) document.get("status"));
                        Log.d("INFO", "Game status has changed.");
                    } else {
                        Log.d("INFO", "Game status is unchanged.");
                    }
                } else {
                    Log.d("INFO", "No document found.");
                }
            } else {
                Log.d("ERROR", "get failed with ", task.getException());
            }
        });
    }

    public void getUpdatedGameScores() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("sessions").document(sessionCode);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> scoresMap = (Map<String, Object>) document.get("scores");
                    for (Map.Entry<String, Object> entry : scoresMap.entrySet()) {
                        scores.putIfAbsent(entry.getKey(), (Long) entry.getValue());
                    }
                    Log.d("INFO", "Updated game scores retrieved.");
                } else {
                    Log.d("INFO", "No document found.");
                }
            } else {
                Log.d("ERROR", "get failed with ", task.getException());
            }
        });
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Map<String, Long> getScores() {
        return scores;
    }

    public void setScores(Map<String, Long> scores) {
        this.scores = scores;
    }
}
