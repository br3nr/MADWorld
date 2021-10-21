package com.madworld.view.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.madworld.R;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: OptionsFragment is a fragment on the Map activity that displays
 *          each option the user can select and apply on the map.
 *          These options are Status, Details, Demolish and TimeStep
 */
public class OptionsFragment extends Fragment
{

    public OptionsFragment() {
        // Required empty public constructor
    }

    // Depending on which option is clicked, call the listeners in MapActivity
    public interface optionFragmentListener
    {
        void onInfoClick();
        void onDetailsClick();
        void onDemolitionClick();
        void onTimeStepClick();
    }

    private optionFragmentListener listener;


    @Override
    // Ensure that the listener is not null on the attach
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof SelectorFragment.SelectorFragmentListener)
        {
            listener = (optionFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Interface was not implemented.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_map_options, container, false);
        // Inflate the layout for this fragment

        //Setup the items in the view
        final ImageView infoButton, detailsButton, demolitionButton, timeStep;

        infoButton = (ImageView) v.findViewById(R.id.options_button);
        detailsButton = (ImageView) v.findViewById(R.id.details_button);
        demolitionButton = (ImageView) v.findViewById(R.id.demolition_button);
        timeStep = (ImageView) v.findViewById(R.id.timestep_button);

        // Call the InfoButton interface method on click
        infoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onInfoClick();
            }
        });

        // Call the Detail interface method on click
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDetailsClick();
            }
        });

        // Call the Demolition interface method on click
        demolitionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                listener.onDemolitionClick();
            }
        });

        // Call the Time Step interface method on click
        timeStep.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                listener.onTimeStepClick();
            }
        });
        return v;
    }
}