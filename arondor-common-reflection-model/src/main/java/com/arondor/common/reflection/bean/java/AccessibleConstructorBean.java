package com.arondor.common.reflection.bean.java;

import java.util.List;

import com.arondor.common.reflection.model.java.AccessibleConstructor;

public class AccessibleConstructorBean implements AccessibleConstructor
{

    /**
     * 
     */
    private static final long serialVersionUID = -3238305306015874730L;

    public AccessibleConstructorBean()
    {

    }

    private List<String> argumentTypes;

    public List<String> getArgumentTypes()
    {
        return argumentTypes;
    }

    public void setArgumentTypes(List<String> argumentTypes)
    {
        this.argumentTypes = argumentTypes;
    }

}
