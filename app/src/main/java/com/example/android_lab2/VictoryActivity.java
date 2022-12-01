package com.example.android_lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VictoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_game_layout);

        Button newGameBtn = (Button)findViewById(R.id.newGameButton);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            // start new game
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VictoryActivity.this, SelectDifficultyActivity.class));
                finish();
            }
        });

        Button mainMenuButton = (Button)findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            // back to main menu
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VictoryActivity.this, MainMenuActivity.class));
                finish();
            }
        });

    }
}
