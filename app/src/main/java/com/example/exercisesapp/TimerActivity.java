package com.example.exercisesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    // -------------------- global variables -------------------- //

    private static final String TIME_LEFT = "millis";
    private static final String TIMER_RUNNING = "running";
    private static final String END_TIME = "end";
    private static final String SHARED_PREFS = "prefs";
    private static final String START_TIME = "start";
    private long startTimeInMillis;
    private long timeLeftInMilli;
    private long endTime;
    private boolean timerRunning = false;

    // -------------------- define views -------------------- //

    // main content
    private TextView tvTimer;
    private Button btnTimer;
    private Button btnReset;
    private Button btnSet;
    private EditText etTimerInput;
    private CountDownTimer countDownTimer;


    // bottom nav bar
    NavigationBarView navigationBarView;

    // top action bar
    ImageView menuIcon;
    TextView tvActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // initialize views
        init();

        // button click listeners
        btnSet.setOnClickListener(view -> {
            String input = etTimerInput.getText().toString();
            if(input.length() == 0) {
                Toast.makeText(TimerActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            long millisInput = Long.parseLong(input) * 60000;

            if(millisInput == 0) {
                Toast.makeText(TimerActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            setTime(millisInput);
            etTimerInput.setText("");
        });

        btnTimer.setOnClickListener(view -> startStop());

        btnReset.setOnClickListener(view -> resetTimer());

        updateTimer();


        // set action bar views
        menuIcon.setVisibility(View.INVISIBLE);
        tvActionBar.setText(R.string.timer_page);

        // bottom nav bar
        navigationBarView.setSelectedItemId(R.id.timer);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /**
             * Switches the activity to the selected icon
             * @param item The selected item
             * @return true if a switch case is taken
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.profile):
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case (R.id.timer):
                        return true;

                    case (R.id.home):
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }

    /**
     * set timer
     * @param millis time in milliseconds
     */
    private void setTime(long millis) {
        startTimeInMillis = millis;
        resetTimer();
        closeKeyboard();
    }

    public void startStop() {
        if(timerRunning) {
            stopTimer();
        }
        else {
            startTimer();
        }
    }

    /**
     * start the timer
     */
    public void startTimer() {

        endTime = System.currentTimeMillis() + timeLeftInMilli;

        countDownTimer = new CountDownTimer(timeLeftInMilli, 1000) {
            @Override
            public void onTick(long l) {
                System.out.println("UPDATED");
                timeLeftInMilli = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                btnTimer.setText(R.string.timerStart);
                btnTimer.setVisibility(View.INVISIBLE);
            }
        }.start();

        updateTimer();
        timerRunning = true;
    }

    /**
     * stop the timer
     */
    public void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        updateTimer();
    }

    /**
     * update the timer
     */
    public void updateTimer() {

        if(timerRunning) {
            btnTimer.setText(R.string.timerPause);
            etTimerInput.setVisibility(View.INVISIBLE);
            btnSet.setVisibility(View.INVISIBLE);
        }
        else {
            btnTimer.setText(R.string.timerStart);
            etTimerInput.setVisibility(View.VISIBLE);
            btnSet.setVisibility(View.VISIBLE);
        }

        // convert to mins and seconds
        int hours = (int) (timeLeftInMilli / 1000) / 3600;
        int mins = (int) ((timeLeftInMilli / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMilli / 1000) % 60;

        String timeLeftText;

        if(hours > 0) {
            timeLeftText = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, mins, seconds);
        }
        else {
            timeLeftText = String.format(Locale.getDefault(), "%02d:%02d", mins, seconds);
        }

        tvTimer.setText(timeLeftText);

    }

    /**
     * reset the timer
     */
    public void resetTimer() {
        if(timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
        }
        timeLeftInMilli = startTimeInMillis;
        updateTimer();
        btnTimer.setVisibility(View.VISIBLE);
    }

    /**
     * initialize variables
     */
    public void init() {

        // main content
        tvTimer = findViewById(R.id.tvTimer);
        btnTimer = findViewById(R.id.btnTimer);
        btnReset = findViewById(R.id.btnReset);
        btnSet = findViewById(R.id.btnTimerSet);
        etTimerInput = findViewById(R.id.etTimerInput);

        // top action bar
        menuIcon = findViewById(R.id.menuOrAddIcon);
        tvActionBar = findViewById(R.id.pageTitle);

        // bottom nav bar
        navigationBarView = findViewById(R.id.bottom_nav);

    }

    /**
     * close the keyboard
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();


        // create shared pref
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // put values in shared prefs
        editor.putLong(START_TIME, startTimeInMillis);
        editor.putLong(TIME_LEFT, timeLeftInMilli);
        editor.putBoolean(TIMER_RUNNING, timerRunning);
        editor.putLong(END_TIME, endTime);

        editor.apply();

        if(countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // create shared pref
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        startTimeInMillis = prefs.getLong(START_TIME, 600000);
        timeLeftInMilli = prefs.getLong(TIME_LEFT, startTimeInMillis);
        timerRunning = prefs.getBoolean(TIMER_RUNNING, false);

        updateTimer();

        if(timerRunning) {
            endTime = prefs.getLong(END_TIME, 0);
            timeLeftInMilli = endTime - System.currentTimeMillis();

            if(timeLeftInMilli < 0) {
                timeLeftInMilli = 0;
                timerRunning = false;
                updateTimer();
            }
            else {
                startTimer();
            }
        }
    }
}