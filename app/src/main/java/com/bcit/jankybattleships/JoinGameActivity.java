package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bcit.jankybattleships.data.GameSession;
import com.bcit.jankybattleships.data.GameStatus;
import com.bcit.jankybattleships.data.Player;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class JoinGameActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        TextView label = findViewById(R.id.textView_join_codelabel);
        EditText editText = findViewById(R.id.editText_join);
        if (MainActivity.DARK_MODE) {
            getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            label.setTextColor(Color.WHITE);
            editText.setTextColor(Color.WHITE);
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            label.setTextColor(Color.BLACK);
            editText.setTextColor(Color.BLACK);
        }

        Button button = findViewById(R.id.button_join_submit);
        button.setOnClickListener(v -> joinGameSession());
    }

    private void joinGameSession() {
        final GameSession session = new GameSession();
        TextView errorTextView = findViewById(R.id.textView_join_error);
        EditText editText = findViewById(R.id.editText_join);
        String code = editText.getText().toString();

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("sessions").document(code);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                editText.setText("");
                if (document.exists()) {
                    errorTextView.setText("");
                    session.setSessionCode(document.getId());
                    session.updateGameStatus(GameStatus.SHIP_PLACEMENT);
                    session.updateGameScoreForPlayer(2, 0);

                    Intent intent = new Intent(this, GameSetupActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable(GameSession.EXTRA_NEW_GAME_SESSION, session);
                    extras.putSerializable(Player.EXTRA_PLAYER,
                            new Player(session.getUserIdOrAnonString(2), 2));
                    intent.putExtras(extras);
                    startActivity(intent);
                    Log.d("INFO", "Game session found.");
                } else {
                    errorTextView.setText(R.string.invalid_room_code);
                    Log.d("INFO", "No game session matching code found.");
                }
            } else {
                Log.d("ERROR", "get failed with ", task.getException());
            }
        });
    }
}