package com.zca.keepingscore.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.zca.keepingscore.R;

import java.util.Locale;

public class DashboardFragment extends Fragment {

    private static final long START_TIME_IN_MILLIS = 5000;
    private TextView mTextViewCountDown;
    private ImageButton mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    /**
    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    int defaultValue = getResources().getInteger(R.integer.saved_times_pressed_default_key);
    int timesPressed = sharedPref.getInt(getString(R.string.saved_button_press_count_key), defaultValue);
     **/

    int score = 0;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        final TextView highscoreView = root.findViewById(R.id.high_score);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.newInt);
        int highscore = sharedPref.getInt(getString(R.string.newString), defaultValue);
        highscoreView.setText("High score: " + Integer.toString(highscore));

        mTextViewCountDown = root.findViewById(R.id.text_view_countdown);

        mButtonStartPause = root.findViewById(R.id.button_mash);
        mButtonReset = root.findViewById(R.id.button_reset);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    score++;
                    textView.setText("Score: " + score);
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDownText();

    /**


                // be amazing, do something
                textView.setText("Button has been pressed " + Integer.toString(newTimesPressed)+ " times!");
**/
        return root;
    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);


                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                int defaultValue = getResources().getInteger(R.integer.saved_times_pressed_default_key);
                int high_score = sharedPref.getInt(getString(R.string.saved_button_press_count_key), defaultValue);
                SharedPreferences.Editor editor = sharedPref.edit();

                if(score > high_score){
                    editor.putInt(getString(R.string.saved_button_press_count_key), score);
                    editor.commit();
                    TextView highscoreView = getActivity().findViewById(R.id.high_score);
                    highscoreView.setText("High score: " + Integer.toString(score));
                }
                score = 0;
            }
        }.start();

        mTimerRunning = true;
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        TextView highscoreView = getActivity().findViewById(R.id.text_dashboard);
        highscoreView.setText("Score: 0");
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }
}