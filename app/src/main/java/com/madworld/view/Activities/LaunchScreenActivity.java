package com.madworld.view.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.madworld.R;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: Acts as the start point of the App. Allows the user to choose
 *          to go to the game or change the settings.
 */
public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    /* NAME : onCreate
     * IMPORTS : savedInstanceState
     * PURPOSE : Starts the launcher screen */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        // Button to open the map
        Button start_button = (Button) findViewById(R.id.menu_start_button);
        start_button.setText("START GAME");

        // Button to open the settings
        Button settings_button = (Button) findViewById(R.id.menu_settings_button);
        settings_button.setText("SETTINGS");

        final Intent map_activity = new Intent(this, MapActivity.class);
        final Intent settings_activity = new Intent(this, SettingsActivity.class);

        // If start button is clicked, launch the map
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(map_activity);
            }
        });

        // Otherwise open the settings
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(settings_activity);
            }
        });
    }
}