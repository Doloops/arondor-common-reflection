package com.arondor.common.reflection.noreflect.testclasses;

public class TestClassB
{
    private TestClassA nestedClassA;

    public TestClassA getNestedClassA()
    {
        return nestedClassA;
    }

    public void setNestedClassA(TestClassA nestedClassA)
    {
        this.nestedClassA = nestedClassA;
    }

}
