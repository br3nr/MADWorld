package com.madworld.model.structures;

import android.os.Parcel;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: Commercial child of the Structure class
 */
public class Commercial extends Structure
{
    // CONSTRUCTOR
    public Commercial(int drawableId, String label) {
        super(drawableId, label);
    }

    /**
     * PARCELABLE IMPLEMENTATION
     **/
    protected Commercial(Parcel in)
    {
        super(in);
    }

    public static final Creator<Commercial> CREATOR = new Creator<Commercial>()
    {
        @Override
        public Commercial createFromParcel(Parcel parcel)
        {
            return new Commercial(parcel);
        }

        @Override
        public Commercial[] newArray(int size)
        {
            return new Commercial[size];
        }
    };
}
