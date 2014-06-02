package com.arondor.common.reflection.noreflect.testclasses;

import java.util.List;

/**
 * Test class which contains a {@link List} of {@link TestClassA}
 * 
 */
public class TestClassD
{
    private List<TestClassA> listClassA;

    public List<TestClassA> getListClassA()
    {
        return listClassA;
    }

    public void setListClassA(List<TestClassA> listClassA)
    {
        this.listClassA = listClassA;
    }

}
