package com.arondor.common.reflection.reflect.testclasses;

/**
 * Same as {@link TestClassA} but with default field values.
 * 
 * @author franc
 *
 */
public class TestClassA1
{
    private String property1 = "DefaultString";

    private long property2 = 12345l;

    public TestClassA1()
    {

    }

    public TestClassA1(String property1)
    {
        setProperty1(property1);
    }

    public TestClassA1(String property1, long property2)
    {
        setProperty1(property1);
        setProperty2(property2);
    }

    public String getProperty1()
    {
        return property1;
    }

    public void setProperty1(String property1)
    {
        this.property1 = property1;
    }

    public long getProperty2()
    {
        return property2;
    }

    public void setProperty2(long property2)
    {
        this.property2 = property2;
    }

}