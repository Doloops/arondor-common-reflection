/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.arondor.common.reflection.noreflect.runtime;

/**
 * Utility class to convert any string-based primitive representation to a
 * primitive value
 * 
 * @author francois
 * 
 */
public final class PrimitiveStringConverter
{
    /**
     * Utility classes should not have a public or default constructor.
     */
    private PrimitiveStringConverter()
    {

    }

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
