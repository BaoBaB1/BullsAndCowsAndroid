package com.example.android_lab2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MainGameWorker extends Worker {

    static final String GENERATED_NUMBER = "generated_number";
    static final String BULLS = "bulls";
    static final String COWS = "cows";
    static final String NUMBER_LEN = "number_len";
    static final String USER_INPUT = "user_input";
    static final String CONTEXT = "context";
    static final String ERROR_MSG = "error";

    public MainGameWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    public Data compareGeneratedNumberAndUserInput(final int user_number, final int generated_number) {
        int[] digits = new int[10];
        int tmp_user_number = user_number;

        while (tmp_user_number > 0) {
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
        return new Data.Builder().putInt(COWS, cows).putInt(BULLS, bulls).build();
    }

    public Data generateRandomNumber(int len) {
        if (len < 4 || len > 6)
            throw new RuntimeException("");
        int num = 0;
        int rank = 1;
        int digits[] = new int[10];
        for (int i = 0; i < len - 1; ++i)
            rank *= 10;
        do {
            int digit = (int) ((Math.random() * (9)) + 0);
            System.out.println("random digit = " + digit);
            if (digits[digit] == 0 && !(num == 0 && digit == 0)) {
                digits[digit] = 1;
                num += digit * rank;
                rank /= 10;
                --len;
            }
        } while (len > 0);
        return new Data.Builder().putInt(GENERATED_NUMBER, num).build();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            System.out.println("MainGameWorker doWork is called");
            // context == 1 => generate random number ;
            // context == 2 => compare numbers ;
            int context = getInputData().getInt(CONTEXT, -1);
            assert (context == 1 || context == 2);
            Data returnData;
            if (context == 1) {
                int number_len = getInputData().getInt(NUMBER_LEN, -1);
                assert (number_len >= 4 && number_len <= 6);
                returnData = generateRandomNumber(number_len);
            } else {
                int user_number = getInputData().getInt(USER_INPUT, -1);
                int generated_number = getInputData().getInt(GENERATED_NUMBER, -1);
                assert (user_number != -1 && generated_number != -1);
                returnData = compareGeneratedNumberAndUserInput(user_number, generated_number);
            }
            return Result.success(returnData);
        } catch (Exception e) {
            Data errorData = new Data.Builder().putString(ERROR_MSG, e.getMessage()).build();
            return Result.failure(errorData);
        }
    }
}
