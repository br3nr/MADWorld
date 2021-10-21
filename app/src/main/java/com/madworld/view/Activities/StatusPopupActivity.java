package com.madworld.view.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.os.Bundle;
import android.widget.TextView;

import com.madworld.R;
import com.madworld.model.GameData;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: StatusPopupActivity is a popup activity that is displayed when
 *          the status button is clicked.
 *
 *          Displays the current status of the world. .
 */
public class StatusPopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_popup);
        GameData gameData = GameData.get();

        // Setup the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.5),(int)(width*0.5));

        // Set up each text views
        TextView timeText, moneyText, incomeText, populationText, employmentText, cityText, temperatureText;

        // Map them to their IDs
        timeText = (TextView) findViewById(R.id.game_time);
        moneyText = (TextView) findViewById(R.id.money);
        incomeText = (TextView) findViewById(R.id.income);
        populationText = (TextView) findViewById(R.id.population);
        employmentText = (TextView) findViewById(R.id.employment);
        cityText = (TextView) findViewById(R.id.city);
        temperatureText = (TextView) findViewById(R.id.temperature);

        // Set the text based off the Data
        timeText.setText("Time: " + gameData.getTime());
        moneyText.setText("Money: " + gameData.getMoney());
        incomeText.setText("Income: " + Double.toString(gameData.getIncome()));
        populationText.setText("Population: " + gameData.getPopulation());
        employmentText.setText("Employment: " + Double.toString(Math.floor(gameData.getEmployment() * 100)) + "%");
        cityText.setText("City: " + gameData.getCity());
        temperatureText.setText("Temperature: " + gameData.getTemp() + "°C");
    }
}