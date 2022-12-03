package com.example.android_lab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SelectDifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_layout);

        // return to main menu button
        ImageButton returnBtn = (ImageButton)findViewById(R.id.difficultyCrossBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectDifficultyActivity.this, MainMenuActivity.class));
                finish();
            }
        });
        // difficulty buttons
        setOnClickButtonListener(R.id.easyDifButton, 4);
        setOnClickButtonListener(R.id.mediumDifButton, 5);
        setOnClickButtonListener(R.id.hardDifButton, 6);
    }

    void setOnClickButtonListener(final int buttonId, final int value) {
        Button difficultyButton = (Button)findViewById(buttonId);
        difficultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectDifficultyActivity.this, MainGameActivity.class);
                Bundle args = new Bundle();
                args.putInt("number_len", value);
                intent.putExtras(args);
                startActivity(intent);
                finish();
            }
        });
    }
}
