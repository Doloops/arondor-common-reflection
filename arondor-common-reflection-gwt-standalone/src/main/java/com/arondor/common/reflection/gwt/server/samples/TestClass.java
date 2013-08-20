package com.arondor.common.reflection.gwt.server.samples;

import com.arondor.common.management.mbean.annotation.Description;

public class TestClass
{
    @Description("This is a string property")
    private String aStringProperty;

    @Description("This is a long property")
    private long aLongProperty;

    public String getAStringProperty()
    {
        return aStringProperty;
    }

    public void setAStringProperty(String aStringProperty)
    {
        this.aStringProperty = aStringProperty;
    }

    public long getALongProperty()
    {
        return aLongProperty;
    }

    public void setALongProperty(long aLongProperty)
    {
        this.aLongProperty = aLongProperty;
    }

    @Override
    public String toString()
    {
        return "TestClass [aStringProperty=" + aStringProperty + ", aLongProperty=" + aLongProperty + "]";
    }
}
