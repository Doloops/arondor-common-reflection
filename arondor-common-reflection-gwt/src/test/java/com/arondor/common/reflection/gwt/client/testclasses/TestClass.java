package com.arondor.common.reflection.gwt.client.testclasses;

import com.arondor.common.management.mbean.annotation.Description;

public class TestClass implements TestInterface
{
    @Description("This is a string property")
    private String aStringProperty;

    @Description("This is a long property")
    private long aLongProperty;

    @Description("This is a sub class")
    private SubTestClass subClass;

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

    public SubTestClass getSubClass()
    {
        return subClass;
    }

    public void setSubClass(SubTestClass subClass)
    {
        this.subClass = subClass;
    }
}
