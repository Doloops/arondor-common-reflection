package com.arondor.common.reflection.parser.java.testing;

import com.arondor.common.management.mbean.annotation.Description;
import com.arondor.common.management.mbean.annotation.Mandatory;

@Description("This class if for testing")
public class ClassA implements InterfaceA
{
    @Description("This is an integer")
    @Mandatory
    private int myInt;

    private String myString;

    public int getMyInt()
    {
        return myInt;
    }

    public void setMyInt(int myInt)
    {
        this.myInt = myInt;
    }

    public String getMyString()
    {
        return myString;
    }

    public void setMyString(String myString)
    {
        this.myString = myString;
    }

}
