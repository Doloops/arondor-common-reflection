package com.arondor.common.reflection.gwt.server.samples;

public enum TestEnum
{

    VALUE1("value1"), VALUE2("value2");

    private String value;

    TestEnum(String value)
    {

        this.value = value;

    }

    String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return value;
    }

}
