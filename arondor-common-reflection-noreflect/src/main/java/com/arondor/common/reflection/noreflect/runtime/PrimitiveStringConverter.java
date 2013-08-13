package com.arondor.common.reflection.noreflect.runtime;

public class PrimitiveStringConverter
{
    public static String to_java_lang_String(String value)
    {
        return value;
    }

    public static int to_int(String value)
    {
        return Integer.parseInt(value);
    }

    public static int to_java_lang_Integer(String value)
    {
        return to_int(value);
    }

    public static long to_long(String value)
    {
        return Long.parseLong(value);
    }

    public static long to_java_lang_Long(String value)
    {
        return to_long(value);
    }

    public static boolean to_boolean(String value)
    {
        return Boolean.parseBoolean(value);
    }

    public static boolean to_java_lang_Boolean(String value)
    {
        return to_boolean(value);
    }

    public static float to_float(String value)
    {
        return Float.parseFloat(value);
    }

    public static float to_java_lang_Float(String value)
    {
        return to_float(value);
    }

    public static double to_double(String value)
    {
        return Double.parseDouble(value);
    }

    public static double to_java_lang_Double(String value)
    {
        return to_double(value);
    }

    public static char to_char(String value)
    {
        return value.charAt(0);
    }
}
