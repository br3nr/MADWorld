package com.madworld.model;

import android.util.Log;

import com.madworld.R;
import com.madworld.model.structures.*;
import java.util.Random;

/**
 *  AUTHOR: Max Barker, 19624729
 *  AUTHOR: Curtin Uni, workshop 3
 *
 *  NOTE: I took the implementation from workshop 3 and added to it.
 *        Has a ton more methods, including the formulas for changing
 *        the economy and time of the world.
 */
public class GameData
{
    public static int WIDTH = 30;
    public static int HEIGHT = 10;
    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2, R.drawable.ic_grass3, R.drawable.ic_grass4};

    public void setTemp(String temp)
    {
        this.temperature = temp;
    }

    // INSTANCE OBJECTS
    private static Settings settings;
    private static GameData instance = null;
    private static final Random rng = new Random();

    // WORLD ECONOMICS
    private int population, money;
    private int nResidential, nCommercial;
    private double empRate = 0.0, income = 0.0;

    // WORLD STATE
    public String temperature;
    private String time = "12 AM";
    private int hour;

    // MAP GRID
    private MapElement[][] grid;


    /* ####################### *
     *     INSTANCE METHODS    *
     * ####################### */

    /** NAME: get
     *  IMPORTS: GameData
     *  EXPORTS: None
     *  PURPOSE: Gets the GameData instance, or creates a new one */
    public static GameData get()
    {
        // Make the settings first, otherwise the GameData instance cant generate
        // its variables properly.
        if (settings == null)
        {
            settings = Settings.get();
        }
        // Set the width and the height before loading the instance
        WIDTH = settings.getMapWidth();
        HEIGHT = settings.getMapHeight();
        // Create the instance if it hasn't already
        if (instance == null)
        {
            instance = new GameData(generateGrid(), settings.getInitialMoney());
        }
        return instance;
    }

    /** GameData Alternate Constructor */
    protected GameData(MapElement[][] grid, int initMoney)
    {
        this.money = initMoney;
        this.grid = grid;
    }


    /* ####################### *
     *       TIME LOGIC        *
     * ####################### */

    public void timeStep()
    {
        adjustTime();
        countStructures();
        setEmployment();
        setPopulation();
        setMoney();
        setIncome();
    }

    public void adjustTime()
    {
        if (hour == 24) {
            hour = 0;
        }
        if (hour <= 12) {
            time = hour + " AM";
        } else {
            time = (hour) - 12 + " PM";
        }
        hour++;
    }



    /* ####################### *
     *         BUILDING        *
     *         METHODS         *
     * ####################### */

    /** NAME: countStructures
     *  IMPORTS: None
     *  EXPORTS: None
     *  PURPOSE: Counts up all of the residential and commercial buildings for later
     *           use in calculations */
    public void countStructures()
    {
        int res = 0;
        int com = 0;
        for(int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if(grid[i][j].getStructure() instanceof Residential)
                {
                    res++;
                }
                else if(grid[i][j].getStructure() instanceof Commercial)
                {
                    com++;
                }
            }
        }
        this.nResidential = res;
        this.nCommercial = com;
    }

    /** NAME: purchaseRoad
     *  IMPORTS: None
     *  EXPORTS: None
     *  PURPOSE: subtracts the cost of a Road from the total money*/
    public void purchaseRoad()
    {
        money -= settings.getRoadBuildingCost();
    }

    /** NAME: purchaseResidential
     *  IMPORTS: None
     *  EXPORTS: None
     *  PURPOSE: subtracts the cost of a Residential structure from the total money*/
    public void purchaseResidential()
    {
        money -= settings.getResBuildingCost();
    }

    /** NAME: purchaseCommercial
     *  IMPORTS: None
     *  EXPORTS: None
     *  PURPOSE: subtracts the cost of a Commercial structure from the total money*/
    public void purchaseCommercial()
    {
        money -= settings.getCommBuildingCost();
    }


    /* ####################### *
     *         GETTERS         *
     * ####################### */

    /** NAME: getElement
     *  IMPORTS: int, int
     *  EXPORTS: Element at a given position
     *  PURPOSE: To retrieve the Element for doing operations on */
    public MapElement get(int i, int j)
    {
        return grid[i][j];
    }

    // Money getter
    public int getMoney()
    {
        return this.money;
    }

    // Current time getter
    public String getTime()
    {
        return time;
    }

    // Income getter
    public double getIncome()
    {
        return income;
    }

    // empRate getter
    public double getEmployment()
    {
        return empRate;
    }

    // population getter
    public int getPopulation()
    {
        return population;
    }

    // HEIGHT getter
    public static int getHEIGHT()
    {
        return HEIGHT;
    }

    // WIDTH getter
    public static int getWIDTH()
    {
        return WIDTH;
    }

    // City getter
    public String getCity()
    {
        return "Perth City";
    }

    // Temperature getter
    public String getTemp()
    {
        return temperature;
    }


    /* ####################### *
     *         SETTERS         *
     * ####################### */

    // Overrides the instance
    public static void setInstance(GameData gd)
    {
        instance = gd;
    }

    /** NAME: setIncome
     *  IMPORTS: None
     *  EXPORTS: None
     *  PURPOSE: setIncome will set the income based of the formula. */
    public void setIncome()
    {
        // Formula as specified by the spec
        this.income = population * (((double)empRate * (double)settings.getSalary() * (double)settings.getTaxRate()) - (double)settings.getServiceCost());
    }

    // Replace the MapElement at a given index
    public void set(int i, int j, MapElement inMap) {
        grid[i][j] = inMap;
    }

    // Set the money, based of the last income.
    public void setMoney()
    {
        money = money + (int)income;
    }

    /** NAME: setEmployment
     *  IMPORTS: None
     *  EXPORTS: None
     *  PURPOSE: Sets the employment based of a formula. Catches divisions by zero */
    public void setEmployment()
    {
        try
        {
            // Formula as given by spec
            empRate =  (double)Math.min(1, (double)nCommercial * (double)settings.getShopSize() / (double)population);
        }
        catch(ArithmeticException e)
        {
            // If there is no population, catch a divide by zero
            Log.d("ARITHMETIC EXCEPTION:", "DIV BY 0");
            empRate = 0.0;
        }
    }

    // Population setter
    public void setPopulation()
    {
        // the population is the family size by the number of residential homes
        population = settings.getFamilySize() * nResidential;
    }


    /* ####################### *
     *      WORKSHOP THREE     *
     *         METHODS         *
     * ####################### */

    private static int choose(boolean nsWater, boolean ewWater, boolean diagWater, int nsCoastId, int ewCoastId, int convexCoastId, int concaveCoastId) {
        int id;
        if (nsWater) {
            if (ewWater) {
                id = convexCoastId;
            } else {
                id = nsCoastId;
            }
        } else {
            if (ewWater) {
                id = ewCoastId;
            } else if (diagWater) {
                id = concaveCoastId;
            } else {
                id = GRASS[rng.nextInt(GRASS.length)];
            }
        }
        return id;
    }

    private static MapElement[][] generateGrid() {
        final int HEIGHT_RANGE = 256;
        final int WATER_LEVEL = 112;
        final int INLAND_BIAS = 24;
        final int AREA_SIZE = 1;
        final int SMOOTHING_ITERATIONS = 2;

        int[][] heightField = new int[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                heightField[i][j] =
                        rng.nextInt(HEIGHT_RANGE)
                                + INLAND_BIAS * (
                                Math.min(Math.min(i, j), Math.min(HEIGHT - i - 1, WIDTH - j - 1)) -
                                        Math.min(HEIGHT, WIDTH) / 4);
            }
        }

        int[][] newHf = new int[HEIGHT][WIDTH];
        for (int s = 0; s < SMOOTHING_ITERATIONS; s++) {
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    int areaSize = 0;
                    int heightSum = 0;

                    for (int areaI = Math.max(0, i - AREA_SIZE);
                         areaI < Math.min(HEIGHT, i + AREA_SIZE + 1);
                         areaI++) {
                        for (int areaJ = Math.max(0, j - AREA_SIZE);
                             areaJ < Math.min(WIDTH, j + AREA_SIZE + 1);
                             areaJ++) {
                            areaSize++;
                            heightSum += heightField[areaI][areaJ];
                        }
                    }

                    newHf[i][j] = heightSum / areaSize;
                }
            }

            int[][] tmpHf = heightField;
            heightField = newHf;
            newHf = tmpHf;
        }

        MapElement[][] grid = new MapElement[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                MapElement element;

                if (heightField[i][j] >= WATER_LEVEL) {
                    boolean waterN = (i == 0) || (heightField[i - 1][j] < WATER_LEVEL);
                    boolean waterE = (j == WIDTH - 1) || (heightField[i][j + 1] < WATER_LEVEL);
                    boolean waterS = (i == HEIGHT - 1) || (heightField[i + 1][j] < WATER_LEVEL);
                    boolean waterW = (j == 0) || (heightField[i][j - 1] < WATER_LEVEL);

                    boolean waterNW = (i == 0) || (j == 0) || (heightField[i - 1][j - 1] < WATER_LEVEL);
                    boolean waterNE = (i == 0) || (j == WIDTH - 1) || (heightField[i - 1][j + 1] < WATER_LEVEL);
                    boolean waterSW = (i == HEIGHT - 1) || (j == 0) || (heightField[i + 1][j - 1] < WATER_LEVEL);
                    boolean waterSE = (i == HEIGHT - 1) || (j == WIDTH - 1) || (heightField[i + 1][j + 1] < WATER_LEVEL);

                    boolean coast = waterN || waterE || waterS || waterW ||
                            waterNW || waterNE || waterSW || waterSE;

                    grid[i][j] = new MapElement(
                            !coast,
                            choose(waterN, waterW, waterNW,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_northwest, R.drawable.ic_coast_northwest_concave),
                            choose(waterN, waterE, waterNE,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_northeast, R.drawable.ic_coast_northeast_concave),
                            choose(waterS, waterW, waterSW,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_southwest, R.drawable.ic_coast_southwest_concave),
                            choose(waterS, waterE, waterSE,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_southeast, R.drawable.ic_coast_southeast_concave),
                            null, i, j);
                } else {
                    grid[i][j] = new MapElement(
                            false, WATER, WATER, WATER, WATER, null, i, j);
                }
            }
        }
        return grid;
    }

    public void regenerate() {
        this.grid = generateGrid();
    }

}
