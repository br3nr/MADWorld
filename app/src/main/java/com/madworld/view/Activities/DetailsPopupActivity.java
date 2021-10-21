package com.madworld.view.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.madworld.R;
import com.madworld.model.MapElement;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: DetailsPopupActivity is a popup activity that is displayed when
 *          the Details button in the Options fragment is clicked.
 *
 *          Returns a updated MapElement when a photo is taken or if the
 *          owners name is changed to update it on the RecyclerView.
 */
public class DetailsPopupActivity extends AppCompatActivity
{
    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_THUMBNAIL = 1;
    private Intent thumbnailPhotoIntent;
    private MapElement mapElement;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_popup);

        // As it is a pop-up window, we need to change it's size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // Set the size of the window
        getWindow().setLayout((int)(width*0.6),(int)(width*0.5));

        final TextView coordinates, structureName, ownerPreview;
        ImageView cameraButton;
        Button enterButton;
        final EditText ownerText;
        mapElement = (MapElement) getIntent().getExtras().getParcelable("mapElement");

        // Set the objects via XML IDs
        coordinates = (TextView) findViewById(R.id.details_coordinate);
        structureName = (TextView) findViewById(R.id.details_structure);
        cameraButton = (ImageView) findViewById(R.id.cameraButton);
        enterButton = (Button) findViewById(R.id.owner_text_button);
        ownerText = (EditText) findViewById(R.id.owner_text);
        ownerPreview = (TextView) findViewById(R.id.owner_preview);

        // If the map element is not null, then store the structures name into
        if(mapElement != null)
        {
            structureName.setText("Structure Type : " + mapElement.getStructure().getLabel());
            coordinates.setText("Coordinates : \n X :  " + mapElement.getXcord() + "\n Y :  " + mapElement.getYcord());
        }

        ownerPreview.setText("Owner : " + mapElement.getStructure().getOwner());

        // Camera button onClickListener, starts the camera for the structure
        cameraButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                thumbnailPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(thumbnailPhotoIntent, REQUEST_THUMBNAIL);
            }
        });

        // Enter button onClickListener. Applies the edit-text to the structure when clicked
        enterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Set the edit text to the owners name
                String editText = ownerText.getEditableText().toString();
                mapElement.getStructure().setOwner(editText);
                ownerPreview.setText(editText);
                onBackPressed();
            }
        });
    }

    /* NAME : onActivityResult
     * IMPORTS : Integer, Integer, Integer
     * PURPOSE : Handles what happens when a photo is taken */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL)
        {
            // Set the thumbnail image on the element
            Bitmap thumbnail = (Bitmap) resultIntent.getExtras().get("data");
            mapElement.setImageBitmap(thumbnail);
            onBackPressed();
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, resultIntent);
        }
    }

    @Override
    /* NAME : onBackPressed
     * IMPORTS : None
     * PURPOSE : Send the mapElement and put it back in the database */
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("map", mapElement);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}