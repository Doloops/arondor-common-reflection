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

import com.arondor.common.reflection.model.java.AccessibleClass;

public class ImplementingClass implements Comparable<ImplementingClass>
{
    private final boolean reference;

    private final String fullName;

    private final String baseName;

    private final AccessibleClass clazz;

    public static final ImplementingClass NULL_CLASS = new ImplementingClass(false, null);

    public static final String NULL_VALUE = "null";

    public static final String REFERENCE_PREFIX = "Reference : ";

    public ImplementingClass(boolean reference, AccessibleClass clazz)
    {
        this.reference = reference;
        this.clazz = clazz;
        this.fullName = (clazz != null ? clazz.getName() : "");
        this.baseName = (clazz != null ? clazz.getClassBaseName() : "");
    }

    public boolean isReference()
    {
        return reference;
    }

    public String getFullName()
    {
        return fullName;
    }

    public String getBaseName()
    {
        return baseName;
    }

    @Override
    public int compareTo(ImplementingClass o)
    {
        if (reference && o.reference)
        {
            return fullName.compareTo(o.fullName);
        }
        if (reference)
        {
            return 1;
        }
        if (o.reference)
        {
            return -1;
        }
        if (fullName == null && o.fullName == null)
        {
            return 0;
        }
        if (fullName == null)
        {
            return -1;
        }
        if (o.fullName == null)
        {
            return 1;
        }
        return fullName.compareTo(o.fullName);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ImplementingClass)
        {
            return compareTo((ImplementingClass) o) == 0;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode()
    {
        return (reference ? 23 : 17) + ((fullName != null) ? fullName.hashCode() : 0);
    }

    @Override
    public String toString()
    {
        if (reference)
        {
            return REFERENCE_PREFIX + fullName;
        }
        return fullName;
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
            return new ImplementingClass(true, null);
        }
        return new ImplementingClass(false, null);
    }
}
