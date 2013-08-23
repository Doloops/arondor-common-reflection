package com.arondor.common.reflection.util;

public class PrimitiveTypeUtil
{
    private PrimitiveTypeUtil()
    {

    }

    private static final Class<?> PRIMITIVES[] = { java.lang.String.class, java.lang.Long.class,
            java.lang.Integer.class, java.lang.Float.class, java.lang.Boolean.class, java.lang.Double.class,
            java.lang.Character.class, boolean.class, int.class, long.class, double.class, float.class, char.class,
            short.class, java.lang.Short.class, byte.class, java.lang.Byte.class };

    public static boolean isPrimitiveType(String clazzName)
    {
        for (Class<?> primitive : PRIMITIVES)
        {
            if (primitive.getName().equals(clazzName))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isPrimitiveType(Class<?> clazz)
    {
        for (Class<?> primitive : PRIMITIVES)
        {
            if (primitive.equals(clazz))
            {
                return true;
            }
        }
        return false;
    }

}
