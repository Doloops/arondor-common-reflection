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