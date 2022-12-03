package com.example.android_lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);

        Button exitButton = (Button)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });

        Button startGameButton = (Button)findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, SelectDifficultyActivity.class));
                finish();
            }
        });

        Button aboutGameButton = (Button)findViewById(R.id.aboutGameButton);
        aboutGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                MyAlertDialog dlg = MyAlertDialog.newInstance("About game",
                        "In this game you have to guess generated number. This number contains " +
                                "only digits which are not repeatable. There are rules:\n " +
                                "1) if your input has digit which is placed on the same position as in generated number then you get 1 bull.\n" +
                                "2) if your input has digit which is not placed on the same position as in generated number but this digit " +
                                "exists then you get 1 cow.");
                dlg.show(fm, "about_game_dlg");
            }
        });

        Button devInfoButton = (Button)findViewById(R.id.devInfoButton);
        devInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                MyAlertDialog dlg = MyAlertDialog.newInstance("Developer info",
                        "This program was written by Oleksii Hyzha, PI-191 student");
                dlg.show(fm, "about_dev_dlg");
            }
        });
    }
}