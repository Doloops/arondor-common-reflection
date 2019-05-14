package com.arondor.common.reflection.xstream.testing;

import java.util.List;

public class PrimitiveListClass
{
    private List<Integer> integerList;

    private List<Float> floatList;

    private List<String> stringList;

    public List<Integer> getIntegerList()
    {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList)
    {
        this.integerList = integerList;
    }

    public List<Float> getFloatList()
    {
        return floatList;
    }

    public void setFloatList(List<Float> floatList)
    {
        this.floatList = floatList;
    }

    public List<String> getStringList()
    {
        return stringList;
    }

    public void setStringList(List<String> stringList)
    {
        this.stringList = stringList;
    }
}
