package com.arondor.common.reflection.bean.java;

import com.arondor.common.reflection.model.java.AccessibleAnnotation;

public class AccessibleAnnotationBean implements AccessibleAnnotation
{
    private static final long serialVersionUID = 5534014571132939169L;

    private String value;

    public AccessibleAnnotationBean()
    {

    }

    @Override
    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

}
