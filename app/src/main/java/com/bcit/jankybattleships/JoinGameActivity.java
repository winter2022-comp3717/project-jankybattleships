package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JoinGameActivity extends AppCompatActivity {

    private GameSession session = null;
    private String code;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        TextView errorTextView = findViewById(R.id.textView_join_error);
        EditText editText = findViewById(R.id.editText_join);
        Button button = findViewById(R.id.button_join_submit);
        button.setOnClickListener(v -> {
            code = editText.getText().toString();
            GameSession tempSession = GameSession.getGameSessionWithCode(code);
            if (tempSession != null) {
                session = tempSession;
                errorTextView.setText("");
            } else {
                errorTextView.setText(R.string.invalid_room_code);
            }
            editText.setText("");
        });
        if (session != null) {
            Intent intent = new Intent(this, ShipPlacementActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable(MainActivity.EXTRA_NEW_GAME_SESSION, session);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                extras.putSerializable(MainActivity.EXTRA_PLAYER,
                        new Player(user.getUid(), 2));
            } else {
                extras.putSerializable(MainActivity.EXTRA_PLAYER,
                        new Player(String.format("Anon%s%d", session.getSessionCode(), 2), 2));
            }
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}