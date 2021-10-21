package com.madworld.model.structures;

import android.os.Parcel;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: Residential child of the Structure class
 */
public class Residential extends Structure
{

    public Residential(int drawableId, String label)
    {
        super(drawableId, label);
    }

    protected Residential(Parcel in)
    {
        super(in);
    }

    public static final Creator<Residential> CREATOR = new Creator<Residential>()
    {
        @Override
        public Residential createFromParcel(Parcel parcel)
        {
            return new Residential(parcel);
        }

        @Override
        public Residential[] newArray(int size)
        {
            return new Residential[size];
        }
    };

}
