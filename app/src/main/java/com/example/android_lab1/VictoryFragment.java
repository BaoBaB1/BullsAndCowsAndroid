package com.example.android_lab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class VictoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.win_game_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button newGameBtn = (Button) view.findViewById(R.id.newGameButton);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            // start new game
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new SelectDifficultyFragment()).commit();
            }
        });

        Button mainMenuButton = (Button) view.findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            // back to main menu
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(getActivity(), MainActivity.class);
                startActivity(intentMain);
            }
        });
    }
}
