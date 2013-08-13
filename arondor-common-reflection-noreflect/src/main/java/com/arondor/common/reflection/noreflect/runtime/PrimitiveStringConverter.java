package com.arondor.common.reflection.noreflect.runtime;

public class PrimitiveStringConverter
{
    public static String getConvertionMethodFromClassName(String className)
    {
        return "to" + className.replace(".", "");
    }

    public static String tojavalangString(String value)
    {
        return value;
    }

    public static int toint(String value)
    {
        return Integer.parseInt(value);
    }

    public static int tojavalangInteger(String value)
    {
        return toint(value);
    }

    public static long tolong(String value)
    {
        return Long.parseLong(value);
    }

    public static long tojavalangLong(String value)
    {
        return tolong(value);
    }

    public static boolean toboolean(String value)
    {
        return Boolean.parseBoolean(value);
    }

    public static boolean tojavalangBoolean(String value)
    {
        return toboolean(value);
    }

    public static float tofloat(String value)
    {
        return Float.parseFloat(value);
    }

    public static float tojavalangFloat(String value)
    {
        return tofloat(value);
    }

    public static double todouble(String value)
    {
        return Double.parseDouble(value);
    }

    public static double tojavalangDouble(String value)
    {
        return todouble(value);
    }

    public static char tochar(String value)
    {
        return value.charAt(0);
    }
}
