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
