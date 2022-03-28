package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class HostGameActivity extends AppCompatActivity {

    private static final int REFRESH_DELAY = 5000;
    private final GameSession session = new GameSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        Button generateButton = findViewById(R.id.button_host_generate);
        Button checkCodeButton = findViewById(R.id.button_host_checkcode);
        Button startButton = findViewById(R.id.button_host_start);

        generateButton.setOnClickListener(view -> generateNewRoomCode());
        checkCodeButton.setOnClickListener(view -> validateRoomCode());
        startButton.setOnClickListener(view -> startWaitForP2());
    }

    private void generateNewRoomCode() {
        TextView roomCodeTextView = findViewById(R.id.textView_host_roomcode);
        TextView errorTextView = findViewById(R.id.textView_host_error);
        errorTextView.setText("");
        session.setSessionCode(RoomCodeGenerator.generateRoomCode());
        roomCodeTextView.setText(session.getSessionCode());
    }

    private void validateRoomCode() {
        TextView roomCodeTextView = findViewById(R.id.textView_host_roomcode);
        TextView errorTextView = findViewById(R.id.textView_host_error);
        if (session.isUsedRoomCode(roomCodeTextView.getText().toString())) {
            errorTextView.setTextColor(getColor(R.color.error_red));
            errorTextView.setText(R.string.used_room_code_warning);
        } else {
            errorTextView.setTextColor(getColor(R.color.black));
            errorTextView.setText(R.string.valid_room_code);
        }
    }

    private void startWaitForP2() {
        TextView waitTextView = findViewById(R.id.textView_host_wait);
        waitTextView.setVisibility(View.VISIBLE);
        session.setStatus(GameStatus.JOIN_WAIT);
        if (!session.createGameSessionInDb())
            Log.e("ERROR", "Game session document add failed.");
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (session.updateGameStatus()
                        && session.getStatus() == GameStatus.SHIP_PLACEMENT) {
                    Intent intent = new Intent(getApplicationContext(),
                            ShipPlacementActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable(MainActivity.EXTRA_NEW_GAME_SESSION, session);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        extras.putSerializable(MainActivity.EXTRA_PLAYER,
                                new Player(user.getUid(), 1));
                    } else {
                        extras.putSerializable(MainActivity.EXTRA_PLAYER,
                                new Player(String.format("Anon%s%d", session.getSessionCode(), 1),
                                        1));
                    }
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    handler.postDelayed(this, REFRESH_DELAY);
                }
            }
        };
        handler.postDelayed(runnable, REFRESH_DELAY);
    }
}