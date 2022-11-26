package com.example.android_lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button exitButton = (Button)view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();
                System.exit(0);
            }
        });

        Button startGameButton = (Button)view.findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_container, new SelectDifficultyFragment()).commit();
            }
        });

        Button aboutGameButton = (Button)view.findViewById(R.id.aboutGameButton);
        aboutGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MyAlertDialog dlg = MyAlertDialog.newInstance("About game",
                        "In this game you have to guess generated number. This number contains " +
                                "only digits which are not repeatable. There are rules:\n " +
                                "1) if your input has digit which is placed on the same position as in generated number then you get 1 bull.\n" +
                                "2) if your input has digit which is not placed on the same position as in generated number but this digit " +
                                "exists then you get 1 cow.");
                dlg.show(fm, "about_game_dlg");
            }
        });

        Button devInfoButton = (Button)view.findViewById(R.id.devInfoButton);
        devInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MyAlertDialog dlg = MyAlertDialog.newInstance("Developer info",
                        "This program was written by Oleksii Hyzha, PI-191 student");
                dlg.show(fm, "about_dev_dlg");
            }
        });
    }
}
