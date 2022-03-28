package com.bcit.jankybattleships;

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
import java.util.concurrent.atomic.AtomicBoolean;

public class GameSession implements Serializable {

    private String sessionCode = null;
    private GameStatus status = null;

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

    public void updateSessionScoreWithP2() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference df = db.collection("cities").document("DC");
        Map<String, Object> scores = new HashMap<>();
        scores.put(getUserIdOrAnonString(2), 0);
        df.update("scores", scores)
                .addOnSuccessListener(aVoid -> Log.d("INFO",
                        "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("ERROR", "Error updating document", e));
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

    public void updateGameStatus() {
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

    public static GameSession getGameSessionWithCode(String code) {
        GameSession session = new GameSession();
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("sessions").document(code);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    session.setSessionCode(document.getId());
                    session.setStatus(GameStatus.SHIP_PLACEMENT);
                    Log.d("INFO", "Game session found.");
                } else {
                    Log.d("INFO", "No game session matching code found.");
                }
            } else {
                Log.d("ERROR", "get failed with ", task.getException());
            }
        });
        if (session.getSessionCode() == null) {
            return null;
        }
        return session;
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
}
