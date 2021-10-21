package com.madworld.model;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: Settings stores all of the initial settings variables for the GameData creation.
 *          Can later be modified by the user.
 */
public class Settings
{
    private int mapWidth, mapHeight;
    private int initialMoney, familySize, shopSize, salary;
    private double taxRate;
    private int houseBuildingCost;
    private int commBuildingCost;
    private int roadBuildingCost;
    private int serviceCost;

    // Settings instance method
    private static Settings instance;

    /*  DEFAULT CONSTRUCTOR  */
    public Settings()
    {
        this.mapWidth = 30;
        this.mapHeight = 10;
        this.initialMoney = 10000;
        this.familySize = 4;
        this.shopSize = 6;
        this.salary = 10;
        this.taxRate = 0.3;
        this.houseBuildingCost = 100;
        this.commBuildingCost = 500;
        this.roadBuildingCost = 20;
        this.serviceCost = 2;
    }


    /* ####################### *
     *     INSTANCE METHODS    *
     * ####################### */
    public static Settings get()
    {
        if(instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }


    /* ####################### *
     *         GETTERS         *
     * ####################### */

    public int getMapWidth()
    {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getInitialMoney()
    {
        return initialMoney;
    }

    public int getFamilySize()
    {
        return familySize;
    }

    public int getServiceCost()
    {
        return serviceCost;
    }

    public double getTaxRate()
    {
        return taxRate;
    }

    public int getShopSize()
    {
        return shopSize;
    }

    public int getSalary()
    {
        return salary;
    }

    public int getCommBuildingCost()
    {
        return commBuildingCost;
    }

    public int getRoadBuildingCost()
    {
        return roadBuildingCost;
    }

    public int getResBuildingCost()
    {
        return houseBuildingCost;
    }


    /* ####################### *
     *         SETTERS         *
     * ####################### */

    // NOTE: Other setters are not included as they are not used.

    public void setMapWidth(int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight)
    {
        this.mapHeight = mapHeight;
    }

    public void setInitialMoney(int initialMoney)
    {
        this.initialMoney = initialMoney;
    }
}
