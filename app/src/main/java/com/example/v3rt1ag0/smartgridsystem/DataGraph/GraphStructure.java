package com.example.v3rt1ag0.smartgridsystem.DataGraph;

/**
 * Created by v3rt1ag0 on 1/2/18.
 */

public class GraphStructure
{
    private int Date;
    private int Value;

    GraphStructure()
    {

    }

    GraphStructure(int Date, int Value)
    {
        this.Date = Date;
        this.Value = Value;
    }

    public void setDate(int Date)
    {
        this.Date = Date;
    }

    public void setValue(int Value)
    {
        this.Value = Value;
    }

    public int getDate()
    {
        return Date;
    }

    public int getValue()
    {
        return Value;
    }
}
