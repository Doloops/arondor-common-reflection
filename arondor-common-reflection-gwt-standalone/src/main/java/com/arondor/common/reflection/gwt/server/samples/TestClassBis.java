package com.arondor.common.reflection.gwt.server.samples;

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
}
