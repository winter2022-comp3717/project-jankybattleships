package com.bcit.jankybattleships;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bcit.jankybattleships.fragments.LeaderboardFragment;
import com.bcit.jankybattleships.fragments.MenuFragment;
import com.bcit.jankybattleships.fragments.OptionsFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NEW_GAME_SESSION =
            "com.bcit.jankybattleships.NEW_GAME_SESSION";
    public static final String EXTRA_PLAYER = "com.bcit.jankybattleships.PLAYER";

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseUser user = null;
    public static boolean DARK_MODE = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_main);
        bottomNavigationView.setSelectedItemId(R.id.menuItem_bot_nav_play);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            item.setChecked(true);
            System.out.println(item.getTitle());
            setFragmentFromBottomNavBar(item.getItemId());
            return true;
        });

        Button button = findViewById(R.id.button_main_login);
        button.setOnClickListener(view -> createSignInIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button button = findViewById(R.id.button_main_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            button.setEnabled(false);
            button.setText(R.string.logged_in);
        } else {
            button.setEnabled(true);
            button.setText(R.string.login);
        }

    }

    public void setLightMode(View view) {
        if (DARK_MODE) {
            setTheme(R.style.Theme_JankyBattleshipsDark);
            int color = Color.BLACK;
            view.setBackgroundColor(color);
        } else {
            setTheme(R.style.Theme_JankyBattleships);
            int color = Color.WHITE;
            view.setBackgroundColor(color);
        }
    }

    public void fixButton() {
        Button button = findViewById(R.id.button_main_login);
        button.setEnabled(true);
        button.setText(R.string.login);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult);

    @SuppressLint("NonConstantResourceId")
    private void setFragmentFromBottomNavBar(int itemId) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        switch (itemId) {
            case R.id.menuItem_bot_nav_leaderboard:
                fragmentTransaction.replace(R.id.fragmentContainerView_main,
                        LeaderboardFragment.newInstance());
                break;
            case R.id.menuItem_bot_nav_play:
                fragmentTransaction.replace(R.id.fragmentContainerView_main,
                        MenuFragment.newInstance());
                break;
            case R.id.menuItem_bot_nav_options:
                fragmentTransaction.replace(R.id.fragmentContainerView_main,
                        OptionsFragment.newInstance());
                break;
        }
        fragmentTransaction.commit();
    }

    //onClick for login
    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            assert response != null;
            int error = Objects.requireNonNull(response.getError()).getErrorCode();
            Log.e("Firebase Login", "" + error);
        }
    }

}

//frame layout- overlay login circle?
