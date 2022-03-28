package com.bcit.jankybattleships;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameSession implements Serializable {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String sessionCode = null;
    private GameStatus status = null;

    public GameSession() {}

    public boolean createGameSessionInDb() {
        AtomicBoolean sessionCreated = new AtomicBoolean(false);
        Map<String, Object> room = new HashMap<>();
        room.put("status", status);
        room.put("scores", new HashMap<String, Object>());
        db.collection("sessions").document(sessionCode)
                .set(room)
                .addOnSuccessListener(aVoid -> {
                    sessionCreated.set(true);
                    Log.d("INFO", "New game session added.");
                })
                .addOnFailureListener(e -> {
                    Log.w("ERROR", "Error adding document", e);
                });
        return sessionCreated.get();
    }

    public boolean updateGameStatus() {
        AtomicBoolean gameStatusChanged = new AtomicBoolean(false);
        DocumentReference docRef = db.collection("sessions").document(sessionCode);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (!status.equals(document.get("status"))) {
                        gameStatusChanged.set(true);
                        status = (GameStatus) document.get("status");
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
        return gameStatusChanged.get();
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
