package com.example.android_lab1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainGameFragment extends Fragment {

    TextView bulls_score_view;
    TextView cows_score_view; 
    TextView difficulty_level_view;
    TextView history_view;
    EditText user_input_field;
    int number_len;
    int steps;
    int generated_number;

    public static MainGameFragment newInstance(int number_len) {
        MainGameFragment mainGameFragment = new MainGameFragment();
        Bundle args = new Bundle();
        // put user selected difficulty number length into argument to get it in onCreate() method
        args.putInt("number_len", number_len);
        mainGameFragment.setArguments(args);
        return mainGameFragment;
    }

    public MainGameFragment() {
        generated_number = -1;
        steps = 1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //System.out.println(getClass().getName() + " onCreate");
        super.onCreate(savedInstanceState);
        this.number_len = getArguments().getInt("number_len", -1);
        //System.out.println("number_len = " + this.number_len);
    }

    protected void resetFields() {
        generated_number = 0;
        steps = 1;
        // clear user input
        user_input_field.getText().clear();
        bulls_score_view.setText("Bulls: 0");
        cows_score_view.setText("Cows: 0");
        history_view.setText("");
    }

    protected int generateRandomNumber(int len) {
        if (len < 4 || len > 6)
            return -1;
        int num = 0;
        int rank = 1;
        int digits[] = new int[10];
        for (int i = 0; i < 10; ++i)
            digits[i] = 0;
        for (int i = 0; i < len - 1; ++i)
            rank *= 10;
        do {
            int digit = (int) ((Math.random() * (9)) + 0);
            System.out.println("random digit = " + digit);
            if (digits[digit] == 0) {
                digits[digit] = 1; 
                num += digit * rank;
                rank /= 10;
                --len;
            }
        } while (len > 0);
        return num; 
    }

    @SuppressLint("SetTextI18n")
    public void startGame() {
        if (number_len < 4 || number_len > 6)
            throw new RuntimeException("(number_len < 4 || number_len > 6); number_len = " + number_len);
        resetFields();
        InputFilter[] filterArray = new InputFilter[1];
        // set max input len in EditText
        filterArray[0] = new InputFilter.LengthFilter(number_len);
        // output text about current difficulty level
        if (number_len == 4) {
            difficulty_level_view.setText("Difficulty: easy (4 digits)");
        } else if (number_len == 5) {
            difficulty_level_view.setText("Difficulty: medium (5 digits)");
        } else {
            difficulty_level_view.setText("Difficulty: hard (6 digits)");
        }
        user_input_field.setFilters(filterArray);
        generated_number = generateRandomNumber(number_len);
        System.out.println("generated number = " + generated_number);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        System.out.println(getClass().getName() + "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        bulls_score_view = (TextView) view.findViewById(R.id.bullsScore);
        cows_score_view = (TextView) view.findViewById(R.id.cowsScore);
        history_view = (TextView) view.findViewById(R.id.historyJournal);
        difficulty_level_view = (TextView) view.findViewById(R.id.difficultyLabel);
        user_input_field = (EditText) view.findViewById(R.id.userInputField);

        ImageButton mainMenuButton = (ImageButton) view.findViewById(R.id.endGameCrossBtn);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(getActivity(), MainActivity.class);
                startActivity(intentMain);
            }
        });

        ImageButton restartGameButton = (ImageButton) view.findViewById(R.id.restartButton);
        restartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft.replace(R.id.fragment_container, new SelectDifficultyFragment()).commit();
            }
        });

        startGame();
        ImageButton checkInputBtn = (ImageButton) view.findViewById(R.id.checkButton);
        checkInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            // get user input and compare with generated number
            public void onClick(View view) {
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                String user_number_str = user_input_field.getText().toString();

                if (user_number_str.length() < number_len) {
                    MyAlertDialog dlg = MyAlertDialog.newInstance("Input error",
                            "Number must have exactly " + number_len + " digits");
                    dlg.show(fm, null);
                    return;
                }
                if (user_number_str.charAt(0) == '0') {
                    MyAlertDialog dlg = MyAlertDialog.newInstance("Input error",
                            "No leading zeros allowed");
                    dlg.show(fm, null);
                    return;
                }
                for (int i = 0; i < user_number_str.length(); ++i) {
                    if (!Character.isDigit(user_number_str.charAt(i))) {
                        MyAlertDialog dlg = MyAlertDialog.newInstance("Input error",
                                "Character " + user_number_str.charAt(i) + " is not a digit");
                        dlg.show(fm, null);
                        return;
                    }
                }
                //System.out.println("user number = " + user_number);
                int user_number = Integer.parseInt(user_number_str);
                int[] digits = new int[10];
                int tmp_user_number = user_number;
                while (tmp_user_number > 0) {
                    // this digit already present
                    if (digits[tmp_user_number % 10] > 0) {
                        MyAlertDialog dlg = MyAlertDialog.newInstance("Input error",
                                "Digit " + tmp_user_number % 10 + " is a duplicate");
                        dlg.show(fm, null);
                        return;
                    }
                    ++digits[tmp_user_number % 10];
                    tmp_user_number /= 10;
                }

                int tmp_generated_number = generated_number;
                tmp_user_number = user_number;
                int bulls, cows;
                bulls = cows = 0;
                while (tmp_user_number > 0) {
                    int last_user_input_digit = tmp_user_number % 10;
                    int last_generated_digit = tmp_generated_number % 10;
                    if (last_user_input_digit == last_generated_digit)
                        ++bulls;
                    else if (digits[last_generated_digit] != 0)
                        ++cows;
                    tmp_user_number /= 10;
                    tmp_generated_number /= 10;
                }
                bulls_score_view.setText("Bulls: " + bulls);
                cows_score_view.setText("Cows: " + cows);
                history_view.append(steps + ")" + " Your input " + user_number + " , bulls " + bulls + " , cows " + cows + ".\n");
                // user guessed the number
                if (bulls == number_len) {
                    ft.replace(R.id.fragment_container, new VictoryFragment()).commit();
                } else {
                    // count user attempts
                    ++steps;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_window_layout, container, false);
    }
}
