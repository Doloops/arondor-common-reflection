package com.arondor.common.reflection.reflect.instantiator;

import java.util.HashMap;
import java.util.Map;

public class FastPrimitiveConverter
{
    private interface PrimitiveConverter
    {
        Object convert(String value);
    }

    private final Map<String, PrimitiveConverter> primitiveConverterMap = new HashMap<String, FastPrimitiveConverter.PrimitiveConverter>();

    public FastPrimitiveConverter()
    {
        primitiveConverterMap.put("java.lang.String", new PrimitiveConverter()
        {
            public Object convert(String value)
            {
                return value;
            }
        });
        primitiveConverterMap.put("long", new PrimitiveConverter()
        {
            public Object convert(String value)
            {
                return Long.parseLong(value);
            }
        });
        primitiveConverterMap.put("int", new PrimitiveConverter()
        {
            public Object convert(String value)
            {
                return Integer.parseInt(value);
            }
        });
        primitiveConverterMap.put("boolean", new PrimitiveConverter()
        {
            public Object convert(String value)
            {
                return Boolean.parseBoolean(value);
            }
        });
        primitiveConverterMap.put("float", new PrimitiveConverter()
        {
            public Object convert(String value)
            {
                return Float.parseFloat(value);
            }
        });
    }

    public Object convert(String value, String primitiveClass)
    {
        PrimitiveConverter converter = primitiveConverterMap.get(primitiveClass);
        if (converter == null)
        {
            throw new IllegalArgumentException("Not supported : primitiveClass=" + primitiveClass);
        }
        return converter.convert(value);
    }
}
