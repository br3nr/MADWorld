package com.madworld.view.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.madworld.R;
import com.madworld.model.GameData;
import com.madworld.model.MapElement;
import com.madworld.model.Settings;
import com.madworld.model.structures.*;
import com.madworld.view.Fragments.MapFragment;
import com.madworld.view.Fragments.OptionsFragment;
import com.madworld.view.Fragments.SelectorFragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.net.ssl.HttpsURLConnection;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: MapActivity is the Activity that displays the Map, the Options fragment, the MapFragment,
 *          and the Selector Fragment. Allows the user to click options, or select Structures from
 *          the selector, and place them / destroy them from the map.
 */
public class MapActivity extends AppCompatActivity implements SelectorFragment.SelectorFragmentListener, MapFragment.MapFragmentListener, OptionsFragment.optionFragmentListener
{
    // Fragment fields
    private FragmentManager fm;
    // Stores the most recently clicked structure
    private Structure curStructure;
    private MapFragment mapFrag;

    // Handles what selection has been clicked
    private boolean structureClicked;
    private boolean detailClicked;
    private boolean demolishClicked;

    // Request code for activity result
    private static int REQUEST_CODE = 10;

    // Timer for the game time
    private Timer timeTimer;
    private TimerTask timeTask;


    /**  NAME: onCreate
     *   PURPOSE: loads all of the fragments, starts the temperature reader, starts the world timer
     *              and displays text views on the screen for the timer.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        structureClicked = false;


        Settings settings = Settings.get();

        fm = getSupportFragmentManager();
        //SelectorFragment sf = (SelectorFragment) fm.findFragmentById(R.id.selector);
        mapFrag = (MapFragment) fm.findFragmentById(R.id.map);
        SelectorFragment selectFrag = (SelectorFragment) fm.findFragmentById(R.id.selector);
        OptionsFragment optFrag = (OptionsFragment) fm.findFragmentById(R.id.options);

        // Check that the fragments haven't already been set.
        if (mapFrag == null || selectFrag == null || optFrag == null) {
            mapFrag = new MapFragment();
            selectFrag = new SelectorFragment();
            optFrag = new OptionsFragment();

            fm.beginTransaction().add(R.id.map, mapFrag).commit();
            fm.beginTransaction().add(R.id.selector, selectFrag).commit();
            fm.beginTransaction().add(R.id.options, optFrag).commit();
        }

        // Start the temperature reading
        TemperatureReader tr = new TemperatureReader();
        tr.execute();

        // Text views for the money and time updates
        final TextView money, time;
        money = (TextView) findViewById(R.id.money_tracker);
        time = (TextView) findViewById(R.id.time_tracker);

        // TIMER FOR THE MAP
        // Has an inner runOnUiThread to prevent it crashing as it must run on the main thread.
        // Uses a final gd for the anonymous class.
        final GameData gd = GameData.get();
        timeTimer = new Timer();
        timeTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the economy, and set the text views
                        gd.timeStep();
                        money.setText("Money : " + Integer.toString(gd.getMoney()) + "(+ " + gd.getIncome() + ")");
                        time.setText("Time : " + gd.getTime());
                    }
                });
            }
        };
        // Start the timer
        timeTimer.scheduleAtFixedRate(timeTask, 0, 1000);
    }


    /* NAME: onStructureClicked
     * IMPORTS: Structure
     * PURPOSE:  When a structure is clicked, store it in the activity for later uses*/
    @Override
    public void onStructureClick(Structure structure) {
        structureClicked = true;
        curStructure = structure;
    }

    /* NAME: mapClickListener
     * IMPORTS: Structure
     * PURPOSE: When a element on the selector is clicked, mapClickListener will do something depending
     *          on the field booleans and what was clicked*/
    @Override
    public void mapClickListener(MapElement clickedElement)
    {
        GameData gd = GameData.get();

        // Check that the structure field isn't null and a structure has been clicked
        if (structureClicked && curStructure != null)
        {
            // If the input structure isn't null
            if (clickedElement.getStructure() == null)
            {
                // If the field structure is a road
                if (curStructure instanceof Road)
                {
                    // purchase it
                    gd.purchaseRoad();
                    clickedElement.setStructure(curStructure);
                    curStructure = null;
                }
                else
                {
                    // If the structure placement is a legal location
                    if (checkPlaceable(clickedElement))
                    {
                        // If its a residential, purchase it
                        if (curStructure instanceof Residential)
                        {
                            gd.purchaseResidential();
                        }
                        // If its a commercial, purchase it
                        else if (curStructure instanceof Commercial)
                        {
                            gd.purchaseCommercial();
                        }
                        // Finally set the elements structure to the selected one
                        clickedElement.setStructure(curStructure);
                        curStructure = null;
                    }
                }
            }
        }

        // Otherwise, if the user clicked the detail button
        else if (detailClicked && clickedElement.getStructure() != null)
        {
            // Start the detail popup
            Intent intent = new Intent(this, DetailsPopupActivity.class);
            intent.putExtra("mapElement", (Parcelable) clickedElement);
            startActivityForResult(intent, REQUEST_CODE);
            detailClicked = false;
        }
        // Finally, if they clicked the demolish button
        else if (demolishClicked && clickedElement.getStructure() != null)
        {
            // Demolish the structure by setting it to null
            clickedElement.setStructure(null);
            clickedElement.setImageBitmap(null);
            demolishClicked = false;
        }
    }

    @Override
    /* NAME: onActivityResult
     * IMPORT: Integer, Integer, Intent
     * PURPOSE: When the details popup launches the camera we need to return the
     *          structure from the activity. That way we can update the Map     */
    public void onActivityResult(int requestCode, int resultCode, Intent result)
    {
        // Check the result
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE)
        {
            GameData gd = GameData.get();
            MapElement me = (MapElement) result.getParcelableExtra("map");
            gd.set(me.getXcord(), me.getYcord(), me);
            // Update the dataset
            mapFrag.updateData();
        } else
        {
            // Update the dataset anyway incase the user changed the owners name
            mapFrag.updateData();
            super.onActivityResult(requestCode, resultCode, result);
        }
    }

    /** NAME: checkPlaceable
     *  IMPORTS: MapElement
     *  EXPORTS: None
     *  PURPOSE: Checks if the provided MapElement is adjacent to a road */
    private boolean checkPlaceable(MapElement element) {
        boolean placeable = false;
        GameData md = GameData.get();

        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                try
                {
                    MapElement ele = md.get(element.getXcord() + i, element.getYcord() + j);
                    if (checkRoad(ele))
                    {
                        placeable = true;
                    }
                }
                // Prevent out of bounds arrays
                catch (ArrayIndexOutOfBoundsException e)
                {
                    Log.d("checkPlaceable Error", "TRIED TO ACCESS OUT OF MAP ELEMENT");
                }
            }
        }
        return placeable;
    }

    /** NAME: checkRoad
     *  IMPORTS: MapElement
     *  EXPORTS: Boolean
     *  PURPOSE: Checks if the map element contains a road  */
    public boolean checkRoad(MapElement element)
    {
        if (element.getStructure() != null)
        {
            // Return true if road
            return element.getStructure().getLabel().equals("Road");
        }
        // Otherwise return false
        return false;
    }

    @Override
    // If the info button is clicked, start the Status popup
    public void onInfoClick() {
        GameData gd = GameData.get();
        Intent intent = new Intent(this, StatusPopupActivity.class);
        startActivity(intent);
    }

    @Override
    // If the details button is clicked, let the Activity know
    public void onDetailsClick() {
        this.detailClicked = true;
        this.structureClicked = false;
    }

    @Override
    // If the demolition button is clicked, let the Activity know
    public void onDemolitionClick() {
        this.demolishClicked = true;
    }

    @Override
    // If the timeStep button is clicked, let the Activity know
    public void onTimeStepClick() {
        GameData gd = GameData.get();
        gd.timeStep();
    }

    @Override
    public void onDestroy()
    {
        // Stop the timer!
        timeTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        timeTimer.cancel();
        GameData.setInstance(null);
        super.onBackPressed();
    }

    /**
     * PRIVATE INNER CLASS: TemperatureReader
     * PURPOSE: Allows for the Asynchronous access of the temperature
     * from OpenWeatherMap API.
     */
    private class TemperatureReader extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String temp = "";
            // URL to openweathermap with the API key
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=Perth&APPID=da1b78c376a8589d0977b4eaf4ce65a1&units=metric";
            HttpsURLConnection conn = null;
            try {
                // Create a URL object from the URL String
                URL url = new URL(urlString);

                // Connect to the url
                conn = (HttpsURLConnection) url.openConnection();

                // Throw an IO exception if it fails
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException();
                }

                // Objects for reading from the website stream
                StringBuilder weatherRes = new StringBuilder();
                BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;

                while ((line = buff.readLine()) != null) {
                    // Read each line in the file
                    weatherRes.append(line);
                }

                // Create JSON objects so we can do commands to extract the temperature
                JSONObject jBase = new JSONObject(weatherRes.toString());
                JSONObject temps = jBase.getJSONObject("main");

                // Set the temperature in the GameData instance
                GameData gd = GameData.get();
                gd.setTemp(temps.get("temp").toString());

            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                // Disconnect the connection, if its not null
                if (conn != null)
                {
                    conn.disconnect();
                }
            }
            return temp;
        }
    }
}

