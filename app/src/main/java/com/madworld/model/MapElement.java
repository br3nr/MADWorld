package com.madworld.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.madworld.model.structures.Structure;

/**
 *  CLASS mapElement
 *  SOURCE : Worksheet 3
 *  PURPOSE : The implementation follows workshop 3 however Parcelable has been implemented along
 *            with imageBitmap and ownerName have been added
 */
public class MapElement implements Parcelable
{
    private final boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;
    private Structure structure;
    private int xcord, ycord;

    // Additional fields
    private String ownerName;
    private Bitmap imageBitmap;


    /** ALT CONSTRUCTOR: MapElement
     *  PURPOSE: Builds the MapElement */
    public MapElement(boolean buildable, int northWest, int northEast, int southWest, int southEast, Structure structure, int xcord, int ycord)
    {
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.xcord = xcord;
        this. ycord =ycord;
        this.ownerName = "";
    }


    public boolean isBuildable()
    {
        return buildable;
    }



    /* ####################### *
     *         GETTERS         *
     * ####################### */

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }

    public int getXcord()
    {
        return xcord;
    }

    public int getYcord()
    {
        return ycord;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public Structure getStructure()
    {
        return structure;
    }

    public Bitmap getImageBitmap()
    {
        return imageBitmap;
    }

    /* ####################### *
     *         SETTERS         *
     * ####################### */

    public void setStructure(Structure structure)
    {
        this.structure = structure;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        this.imageBitmap = imageBitmap;
    }



    /* ####################### *
     *        PARCELABLE       *
     *      IMPLEMENTATION     *
     * ####################### */
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeByte((byte) (buildable ? 1 : 0));
        parcel.writeInt(terrainNorthWest);
        parcel.writeInt(terrainSouthWest);
        parcel.writeInt(terrainNorthEast);
        parcel.writeInt(terrainSouthEast);
        parcel.writeParcelable(structure, i);
        parcel.writeInt(xcord);
        parcel.writeInt(ycord);
        parcel.writeParcelable(imageBitmap, i);
    }

    public static final Creator<MapElement> CREATOR = new Creator<MapElement>()
    {
        @Override
        public MapElement createFromParcel(Parcel in) {
            return new MapElement(in);
        }

        @Override
        public MapElement[] newArray(int size) {
            return new MapElement[size];
        }
    };

    protected MapElement(Parcel in) {
        buildable = in.readByte() != 0;
        terrainNorthWest = in.readInt();
        terrainSouthWest = in.readInt();
        terrainNorthEast = in.readInt();
        terrainSouthEast = in.readInt();
        structure = in.readParcelable(Structure.class.getClassLoader());
        xcord = in.readInt();
        ycord = in.readInt();
        imageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }


}
