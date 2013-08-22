package com.arondor.common.reflection.gwt.client.testclasses;

import com.arondor.common.management.mbean.annotation.Description;

public class SubTestClass
{
    @Description("Counter")
    private int count;

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
