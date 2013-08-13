package com.arondor.common.reflection.noreflect.model;

import java.util.List;

/**
 * Object constructor prototype
 * 
 * @author Francois Barre
 * 
 */
public interface ObjectConstructor
{
    /**
     * Create a new instance of an object
     * 
     * @param arguments
     *            the provided arguments
     * @return the new instance of this object
     */
    Object create(List<Object> arguments);
}
