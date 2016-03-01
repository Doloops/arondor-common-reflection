package com.arondor.common.reflection.noreflect.testclasses.async.submodule1;

import com.arondor.common.reflection.noreflect.testclasses.async.submodule0.TestModule1Class1;

public class TestModule2Class2
{
    private TestModule1Class1 testModule1Class1;

    public TestModule1Class1 getTestModule1Class1()
    {
        return testModule1Class1;
    }

    public void setTestModule1Class1(TestModule1Class1 testModule1Class1)
    {
        this.testModule1Class1 = testModule1Class1;
    }
}
