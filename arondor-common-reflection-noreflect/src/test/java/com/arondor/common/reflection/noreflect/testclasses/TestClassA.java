package com.arondor.common.reflection.noreflect.testclasses;

public class TestClassA
{
    private String property1;

    private long property2;

    public TestClassA()
    {

    }

    public TestClassA(String property1)
    {
        setProperty1(property1);
    }

    public TestClassA(String property1, long property2)
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