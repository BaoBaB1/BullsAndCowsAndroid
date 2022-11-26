package com.example.android_lab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SelectDifficultyFragment extends Fragment {

    void setOnClickButtonListener(View view, final int buttonId, final int value) {
        Button difficultyButton = (Button)view.findViewById(buttonId);
        difficultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                MainGameFragment mainGameFragment = MainGameFragment.newInstance(value);
                ft.replace(R.id.fragment_container, mainGameFragment).commit();
            }
        });
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.difficulty_layout, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        System.out.println(getClass().getName() + " onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        // return to main menu button
        ImageButton returnBtn = (ImageButton) view.findViewById(R.id.difficultyCrossBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(getActivity(), MainActivity.class);
                startActivity(intentMain);
            }
        });
        // difficulty buttons
        setOnClickButtonListener(view, R.id.easyDifButton, 4);
        setOnClickButtonListener(view, R.id.mediumDifButton, 5);
        setOnClickButtonListener(view, R.id.hardDifButton, 6);
    }
}
