package com.arondor.common.reflection.reflect.testclasses;

public class TestClassC
{
    public enum EnumValue
    {
        WEST, EAST, BLUE
    }

    private EnumValue enumValue;

    public TestClassC()
    {

    }

    public TestClassC(EnumValue enumValue)
    {
        setEnumValue(enumValue);
    }

    public EnumValue getEnumValue()
    {
        return enumValue;
    }

    public void setEnumValue(EnumValue enumValue)
    {
        this.enumValue = enumValue;
    }

}
