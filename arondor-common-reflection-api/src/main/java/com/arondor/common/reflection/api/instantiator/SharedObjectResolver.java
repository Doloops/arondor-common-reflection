package com.arondor.common.reflection.api.instantiator;

/**
 * Simple SharedObjectResolver Prototype
 * 
 * @author Francois Barre
 * 
 */
public interface SharedObjectResolver
{
    public Object getSharedObject(String name);
}
