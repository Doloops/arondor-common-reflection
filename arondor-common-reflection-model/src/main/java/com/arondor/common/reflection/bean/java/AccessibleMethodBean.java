package com.arondor.common.reflection.bean.java;

import com.arondor.common.reflection.model.java.AccessibleMethod;

public class AccessibleMethodBean implements AccessibleMethod
{
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
