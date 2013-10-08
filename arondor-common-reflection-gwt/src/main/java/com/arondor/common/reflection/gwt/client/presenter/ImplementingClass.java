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
package com.arondor.common.reflection.gwt.client.presenter;

public class ImplementingClass implements Comparable<ImplementingClass>
{
    private final boolean reference;

    private final String name;

    public static final ImplementingClass NULL_CLASS = new ImplementingClass(false, null);

    public static final String NULL_VALUE = "null";

    public static final String REFERENCE_PREFIX = "Reference : ";

    public ImplementingClass(boolean reference, String name)
    {
        this.reference = reference;
        this.name = name;
    }

    public boolean isReference()
    {
        return reference;
    }

    public String getName()
    {
        return name;
    }

    public int compareTo(ImplementingClass o)
    {
        if (reference && o.reference)
        {
            return name.compareTo(o.name);
        }
        if (reference)
        {
            return 1;
        }
        if (o.reference)
        {
            return -1;
        }
        if (name == null && o.name == null)
        {
            return 0;
        }
        if (name == null)
        {
            return -1;
        }
        if (o.name == null)
        {
            return 1;
        }
        return name.compareTo(o.name);
    }

    public boolean equals(Object o)
    {
        if (o instanceof ImplementingClass)
        {
            return compareTo((ImplementingClass) o) == 0;
        }
        return super.equals(o);
    }

    public int hashCode()
    {
        return (reference ? 23 : 17) + ((name != null) ? name.hashCode() : 0);
    }

    public String toString()
    {
        if (reference)
        {
            return REFERENCE_PREFIX + name;
        }
        return name;
    }

    public static ImplementingClass parseImplementingClass(String value)
    {
        if (value.equals(NULL_VALUE))
        {
            return NULL_CLASS;
        }
        else if (value.startsWith(REFERENCE_PREFIX))
        {
            value = value.substring(ImplementingClass.REFERENCE_PREFIX.length());
            return new ImplementingClass(true, value);
        }
        return new ImplementingClass(false, value);
    }
}
