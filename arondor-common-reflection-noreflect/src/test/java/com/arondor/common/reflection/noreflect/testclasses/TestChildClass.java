package com.arondor.common.reflection.noreflect.testclasses;

public class TestChildClass extends TestParentClass
{
    private String childField;

    public String getChildField()
    {
        return childField;
    }

    public void setChildField(String childField)
    {
        this.childField = childField;
    }
}
