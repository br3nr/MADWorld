package com.madworld.model.structures;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: Represents a possible structure to be placed on the map. A structure simply
 *          contains a drawable int reference, and a string label to be shown in the selector.
 *          Also is parcelable, and that includes its children.
 */
public abstract class Structure implements Parcelable
{
    private final int drawableId;
    private String label;
    private String owner;

    /** Alternate Constructor **/
    public Structure(int drawableId, String label)
    {
        this.drawableId = drawableId;
        this.label = label;
        this.owner = "";
    }


    /*  GETTERS  */
    public String getOwner()
    {
        if(owner == null)
        {
            // Set as No Owner on initial creation
            return "No owner.";
        }
        return owner;
    }

    public int getDrawableId()
    {
        return drawableId;
    }

    public String getLabel()
    {
        return label;
    }

    /*  SETTERS  */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    /**  PARCELABLE IMPLEMENTATION  **/
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(drawableId);
        parcel.writeString(label);
        parcel.writeString(owner);
    }

    protected Structure(Parcel in)
    {
        drawableId = in.readInt();
        label = in.readString();
        owner = in.readString();
    }
}
