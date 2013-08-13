package com.arondor.common.reflection.api.hash;


public class NoHashHelper implements HashHelper
{
    public String hashClassName(String className)
    {
        return className;
    }

    public String hashFieldName(String className, String fieldName)
    {
        return fieldName;
    }
}
