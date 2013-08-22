package com.arondor.common.reflection.gwt.server.samples;

import com.arondor.common.management.mbean.annotation.Description;

public class ParentTestClass
{
    @Description("Test Class Field")
    private TestInterface testInterfaceField;

    @Description("Other property")
    private TestClassBis testClassBisField;

    @Description("Primitive property")
    private String primitiveField;


    public TestInterface getTestInterfaceField()
    {
        return testInterfaceField;
    }

    public void setTestInterfaceField(TestInterface testInterfaceField)
    {
        this.testInterfaceField = testInterfaceField;
    }

    public TestClassBis getTestClassBisField()
    {
        return testClassBisField;
    }

    public void setTestClassBisField(TestClassBis testClassBisField)
    {
        this.testClassBisField = testClassBisField;
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
