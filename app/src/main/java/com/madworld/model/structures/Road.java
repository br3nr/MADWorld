package com.madworld.model.structures;

import android.os.Parcel;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: Road child of the Structure class
 */
public class Road extends Structure
{

    public Road(int drawableId, String label) {
        super(drawableId, label);
    }

    /**
     *   PARCELABLE IMPLEMENTATION
     */
    protected Road(Parcel in)
    {
        super(in);
    }

    public static final Creator<Road> CREATOR = new Creator<Road>() {

        @Override
        public Road createFromParcel(Parcel parcel)
        {
            return new Road(parcel);
        }

        @Override
        public Road[] newArray(int size)
        {
            return new Road[size];
        }
    };

}
