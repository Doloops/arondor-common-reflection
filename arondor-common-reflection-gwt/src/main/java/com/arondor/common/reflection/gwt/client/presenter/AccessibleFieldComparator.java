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

import java.io.Serializable;
import java.util.Comparator;

import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;

public class AccessibleFieldComparator implements Comparator<AccessibleField>, Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -4638253874131314167L;

    public int compare(AccessibleField o1, AccessibleField o2)
    {
        boolean prim1 = PrimitiveTypeUtil.isPrimitiveType(o1.getClassName())
                || o1.getClassName().equals("java.util.List");
        boolean prim2 = PrimitiveTypeUtil.isPrimitiveType(o2.getClassName())
                || o2.getClassName().equals("java.util.List");
        if (prim1 && !prim2)
        {
            return 1;
        }
        if (!prim1 && prim2)
        {
            return -1;
        }
        return o1.getName().compareTo(o2.getName());
    }

}