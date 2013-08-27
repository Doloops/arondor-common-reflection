package com.arondor.common.reflection.gwt.server.samples;

import java.util.Map;

import com.arondor.common.management.mbean.annotation.Description;

public class TestClassBis
{
    @Description("This is a String property")
    private String field;

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    private Map<String, TestInterface> mapField;

    public Map<String, TestInterface> getMapField()
    {
        return mapField;
    }

    public void setMapField(Map<String, TestInterface> mapField)
    {
        this.mapField = mapField;
    }

    @Description("A map of String, String")
    private Map<String, String> stringMapField;

    public Map<String, String> getStringMapField()
    {
        return stringMapField;
    }

    public void setStringMapField(Map<String, String> stringMapField)
    {
        this.stringMapField = stringMapField;
    }

}
