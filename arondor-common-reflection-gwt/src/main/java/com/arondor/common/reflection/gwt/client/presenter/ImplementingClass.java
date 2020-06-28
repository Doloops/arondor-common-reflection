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

    private final String className;

    private final String displayName;

    public static final ImplementingClass NULL_CLASS = new ImplementingClass(false, null, null);

    public ImplementingClass(boolean reference, String fullName, String displayName)
    {
        this.reference = reference;
        this.className = fullName;
        this.displayName = displayName;
    }

    public ImplementingClass(AccessibleClass clazz)
    {
        this(false, clazz.getName(), clazz.getClassBaseName());
    }

    public boolean isReference()
    {
        return reference;
    }

    /**
     * Used to retrieve the concatenated package and name of the implementing
     * class
     * 
     * @return the full name (package details + className) of the implementing
     *         class
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Used to retrieve only the name of the implementing class
     * 
     * @return the name (class name) of the implementing class
     */
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public int compareTo(ImplementingClass o)
    {
        if (reference && o.reference)
        {
            if (displayName == null && o.displayName == null)
                return 0;
            if (displayName == null)
                return -1;
            return displayName.compareTo(o.displayName);
        }
        if (reference)
        {
            return 1;
        }
        if (o.reference)
        {
            return -1;
        }
        if (className == null && o.className == null)
        {
            return 0;
        }
        if (className == null)
        {
            return -1;
        }
        if (o.className == null)
        {
            return 1;
        }
        return className.compareTo(o.className);
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
        return (reference ? 23 : 17) + ((className != null) ? className.hashCode() : 0);
    }

    @Override
    public String toString()
    {
        return "{reference=" + reference + ", className=" + className + ", displayName=" + displayName + "}";
    }
}
