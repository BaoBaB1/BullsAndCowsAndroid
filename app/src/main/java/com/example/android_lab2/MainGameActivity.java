package com.example.android_lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.ArrayList;

public class MainGameActivity extends AppCompatActivity {

    TextView bulls_score_view;
    TextView cows_score_view; 
    TextView difficulty_level_view;
    TextView history_view;
    EditText user_input_field;
    int user_input;
    int number_len;
    int steps;
    int generated_number;

    WorkManager workManager;

    boolean validUserInput(String user_number_str) {
        final FragmentManager fm = getSupportFragmentManager();
        if (user_number_str.length() < number_len) {
            MyAlertDialog dlg = MyAlertDialog.newInstance("Input error",
                    "Number must have exactly " + number_len + " digits");
            dlg.show(fm, null);
            return false;
        }
        if (user_number_str.charAt(0) == '0') {
            MyAlertDialog dlg = MyAlertDialog.newInstance("Input error",
                    "No leading zeros allowed");
            dlg.show(fm, null);
            return false;
        }
        ArrayList<Character> user_input_chars = new ArrayList<>();
        for (int i = 0; i < user_number_str.length(); ++i) {
            char current_char = user_number_str.charAt(i);

            if (!Character.isDigit(current_char)) {
                MyAlertDialog dlg = MyAlertDialog.newInstance("Input error",
                        "Character " + user_number_str.charAt(i) + " is not a digit");
                dlg.show(fm, null);
                return false;
            }
            // digit is a duplicate
            if (user_input_chars.contains(current_char)) {
                MyAlertDialog dlg = MyAlertDialog.newInstance("Input error",
                        "Digit " + current_char + " is a duplicate");
                dlg.show(fm, null);
                return false;
            }
            user_input_chars.add(current_char);
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_window_layout);
        // get selected difficulty by user
        Bundle b = getIntent().getExtras();
        assert (b != null);
        this.number_len = b.getInt("number_len");

        bulls_score_view = (TextView)findViewById(R.id.bullsScore);
        cows_score_view = (TextView)findViewById(R.id.cowsScore);
        history_view = (TextView)findViewById(R.id.historyJournal);
        difficulty_level_view = (TextView)findViewById(R.id.difficultyLabel);
        user_input_field = (EditText)findViewById(R.id.userInputField);
        workManager = WorkManager.getInstance();

        InputFilter[] filterArray = new InputFilter[1];
        // set max input len in EditText
        filterArray[0] = new InputFilter.LengthFilter(number_len);
        // output text about current difficulty level
        if (number_len == 4) {
            difficulty_level_view.setText("Difficulty: easy (4 digits)");
        } else if (number_len == 5) {
            difficulty_level_view.setText("Difficulty: medium (5 digits)");
        } else if (number_len == 6){
            difficulty_level_view.setText("Difficulty: hard (6 digits)");
        } else {
            throw new RuntimeException("(number_len < 4 || number_len > 6); number_len = " + number_len);
        }
        user_input_field.setFilters(filterArray);

        ImageButton mainMenuButton = (ImageButton) findViewById(R.id.endGameCrossBtn);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainGameActivity.this, MainMenuActivity.class));
                finish();
            }
        });

        ImageButton restartGameButton = (ImageButton)findViewById(R.id.restartButton);
        restartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainGameActivity.this, SelectDifficultyActivity.class));
                finish();
            }
        });
        ImageButton checkInputBtn = (ImageButton)findViewById(R.id.checkButton);
        checkInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            // get user input and compare with generated number
            public void onClick(View view) {
                String user_number_str = user_input_field.getText().toString();
                if (!validUserInput(user_number_str))
                    return;
                user_input = Integer.parseInt(user_number_str);
                Data.Builder data = new Data.Builder();
                data.putInt(MainGameWorker.CONTEXT, 2); // context == 2 => compare user input with generated value
                data.putInt(MainGameWorker.USER_INPUT, user_input);
                data.putInt(MainGameWorker.GENERATED_NUMBER, generated_number);
                System.out.println("Starting WorkManager to compare user number and generated number");
                startWorkManager(2, data.build());
            }
        });
        startGame();
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

    @SuppressLint("SetTextI18n")
    public void startGame() {
        resetFields();
        Data.Builder data = new Data.Builder();
        data.putInt(MainGameWorker.CONTEXT, 1); // context == 1 => generate new random number
        data.putInt(MainGameWorker.NUMBER_LEN, number_len);
        System.out.println("Starting WorkManager to generate random value");
        startWorkManager(1, data.build());
    }

    private void processNumberGeneratingTaskResult(final WorkInfo workInfo) {
        System.out.println("WorkManager generate number succeeded!");
        generated_number = workInfo.getOutputData().getInt(MainGameWorker.GENERATED_NUMBER, -1);
        System.out.println("generated number = " + generated_number);
    }

    private void processComparisonNumbersTaskResult(final WorkInfo workInfo) {
        System.out.println("WorkManager numbers comparison succeeded!");
        int bulls = workInfo.getOutputData().getInt(MainGameWorker.BULLS, -1);
        int cows = workInfo.getOutputData().getInt(MainGameWorker.COWS, -1);
        assert (bulls != -1 && cows != -1);
        bulls_score_view.setText("Bulls: " + bulls);
        cows_score_view.setText("Cows: " + cows);
        history_view.append(steps + ")" + " Your input " + user_input + " , bulls " + bulls + " , cows " + cows + ".\n");
        // user guessed the number
        if (bulls == number_len) {
            startActivity(new Intent(MainGameActivity.this, VictoryActivity.class));
        } else {
            // count user attempts
            ++steps;
        }
    }

    private void startWorkManager(final int context, final Data data) {
        assert (context == 1 || context == 2);
        WorkRequest request = new OneTimeWorkRequest.Builder(MainGameWorker.class)
                .setInputData(data).build();
        workManager.enqueue(request);
        workManager.getWorkInfoByIdLiveData(request.getId()).observe(this, new androidx.lifecycle.Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null) {
                    if (WorkInfo.State.SUCCEEDED == workInfo.getState()) {
                        if (context == 1) {
                            processNumberGeneratingTaskResult(workInfo);
                        } else if (context == 2) {
                            processComparisonNumbersTaskResult(workInfo);
                        }
                    } else if (WorkInfo.State.FAILED == workInfo.getState()) {
                        throw new RuntimeException(workInfo.getOutputData().getString(MainGameWorker.ERROR_MSG));
                    }
                }
            }
        });
    }
}
