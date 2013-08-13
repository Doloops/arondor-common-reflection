package com.arondor.common.reflection.noreflect.model;

/**
 * Field Setter prototype
 * 
 * @author Francois Barre
 * 
 */
public interface FieldSetter
{
    /**
     * Set a value to an object instance field
     * 
     * @param object
     *            the this object
     * @param value
     *            the value to set for this field
     */
    void set(Object object, Object value);
}
