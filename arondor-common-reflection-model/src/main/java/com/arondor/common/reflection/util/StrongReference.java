package com.arondor.common.reflection.util;

/**
 * Simple, strong, typed reference to an object, as a placeholder to this
 * object.
 * 
 * @author Francois Barre
 *
 */
public class StrongReference<T>
{
    public static final String CLASSNAME = StrongReference.class.getName();

    private final T referent;

    public StrongReference(T referent)
    {
        this.referent = referent;
    }

    public T get()
    {
        return referent;
    }
}
