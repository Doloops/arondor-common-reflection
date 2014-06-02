package com.arondor.common.reflection.noreflect.testclasses;

import java.util.Map;

/**
 * Test class which contains a {@link Map} of {@link String} as keys and
 * {@link TestClassA} as values
 * 
 */
public class TestClassE
{
    private Map<String, TestClassA> mapClassA;

    public Map<String, TestClassA> getMapClassA()
    {
        return mapClassA;
    }

    public void setMapClassA(Map<String, TestClassA> mapClassA)
    {
        this.mapClassA = mapClassA;
    }

}
