package com.zca.keepingscore.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.zca.keepingscore.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.hiscore);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Button button = root.findViewById(R.id.mash_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // THIS line just initializes the sharedprefs
                // local to our current Activity/Fragment/Context
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

                // set a default value for the number of times pressed to 0
                int defaultValue = getResources().getInteger(R.integer.saved_times_pressed_default_key);

                // get the saved value from sharedPref.  If we have no value
                // (never saved this key before) set it to our default of 0
                int timesPressed = sharedPref.getInt(getString(R.string.part_1), defaultValue);

                int newTimesPressed = timesPressed + 1;

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.part_1), newTimesPressed);
                editor.commit();

                // be amazing, do something
                textView.setText("Button has been pressed " + Integer.toString(newTimesPressed)+ " times!");
            }
        });

        return root;
    }
}