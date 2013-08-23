package com.arondor.common.reflection.gwt.client.testclasses;

import java.util.List;

import com.arondor.common.management.mbean.annotation.Description;

public class ParentTestClass
{
    @Description("Test Class Field")
    private TestInterface testInterfaceField;

    @Description("Other property")
    private TestClassBis testClassBisField;

    @Description("Primitive property (String)")
    private String primitiveField;

    @Description("Primitive property")
    private int intField;

    @Description("Primitive property")
    private Integer integerField;

    @Description("Primitive property")
    private long longField;

    @Description("Primitive property")
    private Long longField2;

    @Description("Primitive property")
    private float floatField;

    @Description("Primitive property")
    private Float floatField2;

    @Description("Primitive property")
    private double doubleField;

    @Description("Primitive property")
    private Double doubleField2;

    @Description("Primitive property")
    private boolean boolField;

    @Description("Primitive property")
    private Boolean booleanField;

    @Description("property")
    private List<String> listStringField;

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

    public int getIntField()
    {
        return intField;
    }

    public void setIntField(int intField)
    {
        this.intField = intField;
    }

    public Integer getIntegerField()
    {
        return integerField;
    }

    public void setIntegerField(Integer integerField)
    {
        this.integerField = integerField;
    }

    public long getLongField()
    {
        return longField;
    }

    public void setLongField(long longField)
    {
        this.longField = longField;
    }

    public Long getLongField2()
    {
        return longField2;
    }

    public void setLongField2(Long longField2)
    {
        this.longField2 = longField2;
    }

    public float getFloatField()
    {
        return floatField;
    }

    public void setFloatField(float floatField)
    {
        this.floatField = floatField;
    }

    public Float getFloatField2()
    {
        return floatField2;
    }

    public void setFloatField2(Float floatField2)
    {
        this.floatField2 = floatField2;
    }

    public double getDoubleField()
    {
        return doubleField;
    }

    public void setDoubleField(double doubleField)
    {
        this.doubleField = doubleField;
    }

    public Double getDoubleField2()
    {
        return doubleField2;
    }

    public void setDoubleField2(Double doubleField2)
    {
        this.doubleField2 = doubleField2;
    }

    public boolean isBoolField()
    {
        return boolField;
    }

    public void setBoolField(boolean boolField)
    {
        this.boolField = boolField;
    }

    public Boolean getBooleanField()
    {
        return booleanField;
    }

    public void setBooleanField(Boolean booleanField)
    {
        this.booleanField = booleanField;
    }

    public List<String> getListStringField()
    {
        return listStringField;
    }

    public void setListStringField(List<String> listStringField)
    {
        this.listStringField = listStringField;
    }

}
