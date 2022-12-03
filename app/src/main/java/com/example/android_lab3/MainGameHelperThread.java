package com.example.android_lab3;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.CountDownLatch;

public class MainGameHelperThread extends Thread {

    private static final String TAG = "MainGameHelperThread";
    private Handler handler;

    public static class RandomNumberGenerator implements Runnable {

        private static final String TAG = "RandomNumberGenerator";
        private final CountDownLatch latch;
        int number_len;
        int generated_number;
        
        RandomNumberGenerator(final int number_len, final CountDownLatch latch) {
            this.latch = latch;
            this.number_len = number_len; 
        }

        public int getGeneratedNumber() { return generated_number; }
        
        @Override
        public void run() {
            Log.d(TAG, "Running RandomNumberGenerator");
            generated_number = 0;
            int rank = 1;
            int digits[] = new int[10];
            for (int i = 0; i < number_len - 1; ++i)
                rank *= 10;
            do {
                int digit = (int) ((Math.random() * (9)) + 0);
                Log.d(TAG, "random digit = " + digit);
                if (digits[digit] == 0 && !(generated_number == 0 && digit == 0)) {
                    digits[digit] = 1;
                    generated_number += digit * rank;
                    rank /= 10;
                    --number_len;
                }
            } while (number_len > 0);
            Log.d(TAG, "Random number = " + generated_number);
            Log.d(TAG, "Finished running RandomNumberGenerator");
            latch.countDown();
        }
    }

    public static class NumberComparator implements Runnable {

        private static final String TAG = "NumberComparator";
        private final CountDownLatch latch;
        int user_number;
        int generated_number;
        int bulls;
        int cows;

        NumberComparator(final int user_number, final int generated_number, final CountDownLatch latch) {
            this.latch = latch;
            this.user_number = user_number;
            this.generated_number = generated_number;
        }

        public int getCows() { return cows; }
        public int getBulls() { return bulls; }

        @Override
        public void run() {
            Log.d(TAG, "Running NumberComparator");
            int[] digits = new int[10];
            int tmp_user_number = user_number;

            while (tmp_user_number > 0) {
                ++digits[tmp_user_number % 10];
                tmp_user_number /= 10;
            }
            int tmp_generated_number = generated_number;
            tmp_user_number = user_number;
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
            Log.d(TAG, "Finished running NumberComparator");
            latch.countDown();
        }
    }

    public void addTask(Runnable runnable) {
        if (handler == null)
            handler = new Handler();
        handler.post(runnable);
    }

    @Override
    public void run() {
        Looper.prepare();
        Log.d(TAG, "Initializing looper");
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };
        Looper.loop();
    }
}
