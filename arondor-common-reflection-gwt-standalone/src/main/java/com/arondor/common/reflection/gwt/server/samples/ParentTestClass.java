package com.arondor.common.reflection.gwt.server.samples;

import java.util.ArrayList;
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

    private byte byteField;

    private Byte byteField2;

    private short shortField;

    private Short shortField2;

    private char charField;

    private Character characterField;

    @Description("property")
    private List<String> arraylistStringField = new ArrayList<String>();

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

    public byte getByteField()
    {
        return byteField;
    }

    public void setByteField(byte byteField)
    {
        this.byteField = byteField;
    }

    public Byte getByteField2()
    {
        return byteField2;
    }

    public void setByteField2(Byte byteField2)
    {
        this.byteField2 = byteField2;
    }

    public short getShortField()
    {
        return shortField;
    }

    public void setShortField(short shortField)
    {
        this.shortField = shortField;
    }

    public Short getShortField2()
    {
        return shortField2;
    }

    public void setShortField2(Short shortField2)
    {
        this.shortField2 = shortField2;
    }

    public List<String> getArraylistStringField()
    {
        return arraylistStringField;
    }

    public void setArraylistStringField(List<String> arraylistStringField)
    {
        this.arraylistStringField = arraylistStringField;
    }

    public char getCharField()
    {
        return charField;
    }

    public void setCharField(char charField)
    {
        this.charField = charField;
    }

    public Character getCharacterField()
    {
        return characterField;
    }

    public void setCharacterField(Character characterField)
    {
        this.characterField = characterField;
    }

}
