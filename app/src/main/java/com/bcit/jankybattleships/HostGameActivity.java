package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bcit.jankybattleships.data.GameSession;
import com.bcit.jankybattleships.data.GameStatus;
import com.bcit.jankybattleships.data.Player;
import com.bcit.jankybattleships.data.RoomCodeGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HostGameActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final GameSession session = new GameSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        TextView label = findViewById(R.id.textView_host_codelabel);
        TextView wait = findViewById(R.id.textView_host_wait);

        if (MainActivity.DARK_MODE) {
            getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            label.setTextColor(Color.WHITE);
            wait.setTextColor(Color.WHITE);
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            label.setTextColor(Color.BLACK);
            wait.setTextColor(Color.BLACK);
        }

        Button generateButton = findViewById(R.id.button_host_generate);
        Button startButton = findViewById(R.id.button_host_start);

        generateButton.setOnClickListener(view -> generateNewRoomCode());
        startButton.setOnClickListener(view -> startWaitForP2());
    }

    private void generateNewRoomCode() {
        TextView roomCodeTextView = findViewById(R.id.textView_host_roomcode);
        TextView errorTextView = findViewById(R.id.textView_host_error);
        errorTextView.setText("");
        session.setSessionCode(RoomCodeGenerator.generateRoomCode());
        roomCodeTextView.setText(session.getSessionCode());
        DocumentReference docRef = db.collection("sessions")
                .document(roomCodeTextView.getText().toString());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    errorTextView.setTextColor(getColor(R.color.error_red));
                    errorTextView.setText(R.string.used_room_code_warning);
                    Log.d("INFO", "DocumentSnapshot data: " + document.getData());
                } else {
                    errorTextView.setTextColor(getColor(R.color.black));
                    errorTextView.setText(R.string.valid_room_code);
                    Log.d("INFO", "No such document");
                }
            } else {
                Log.d("INFO", "get failed with ", task.getException());
            }
        });
    }

    private void startWaitForP2() {
        TextView waitTextView = findViewById(R.id.textView_host_wait);
        waitTextView.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        session.setStatus(GameStatus.JOIN_WAIT);
        session.createGameSessionInDb();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                session.getUpdatedGameStatus();
                if (session.getStatus() == GameStatus.SHIP_PLACEMENT) {
                    Intent intent = new Intent(getApplicationContext(),
                            GameSetupActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable(GameSession.EXTRA_NEW_GAME_SESSION, session);
                    extras.putSerializable(Player.EXTRA_PLAYER,
                            new Player(session.getUserIdOrAnonString(1),
                                    1));
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    handler.postDelayed(this, GameSession.REFRESH_DELAY);
                }
            }
        };
        handler.postDelayed(runnable, GameSession.REFRESH_DELAY);
    }
}