package com.madworld.view.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.madworld.R;
import com.madworld.model.Settings;


/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: The activity that displays and allows the user to update the Settings.
 *          Sets the settings onto the GameData's static instance.
 */
public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Settings set = Settings.get();
        final EditText money, width, height;
        Button submitButton;

        // Setup each View widgit
        money = (EditText) findViewById(R.id.editMoney);
        height = (EditText) findViewById(R.id.editHeight);
        width = (EditText) findViewById(R.id.editWidth);
        submitButton = (Button) findViewById(R.id.settingsButton);

        // When they hit submit, commit the changes
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!money.getEditableText().toString().isEmpty() && !height.getEditableText().toString().isEmpty() &&
                    !width.getEditableText().toString().isEmpty())
                {
                    try {
                        set.setMapHeight(Integer.parseInt(height.getEditableText().toString()));
                        set.setMapWidth(Integer.parseInt(width.getEditableText().toString()));
                        set.setInitialMoney(Integer.parseInt(money.getEditableText().toString()));
                    }
                    catch (NumberFormatException e)
                    {
                        Log.d("BAD NUMBER", "RETURNING");
                    }

                    // And go back to the launch screen
                    onBackPressed();
                }
            }
        });
    }
}