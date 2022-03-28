package com.bcit.jankybattleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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
    }
}