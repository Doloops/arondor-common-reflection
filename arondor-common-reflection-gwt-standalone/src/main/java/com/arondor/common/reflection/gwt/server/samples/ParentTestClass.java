package com.arondor.common.reflection.gwt.server.samples;

import com.arondor.common.management.mbean.annotation.Description;

public class ParentTestClass
{
    @Description("Test Class Field")
    private TestClass testClass;

    @Description("Other property")
    private TestClassBis testClassBis;

    @Description("Primitive property")
    private String primitiveField;

    public TestClass getTestClass()
    {
        return testClass;
    }

    public void setTestClass(TestClass testClass)
    {
        this.testClass = testClass;
    }

    public TestClassBis getTestClassBis()
    {
        return testClassBis;
    }

    public void setTestClassBis(TestClassBis testClassBis)
    {
        this.testClassBis = testClassBis;
    }

    public String getPrimitiveField()
    {
        return primitiveField;
    }

    public void setPrimitiveField(String primitiveField)
    {
        this.primitiveField = primitiveField;
    }

}
