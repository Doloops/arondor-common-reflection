package com.arondor.common.reflection.model.config;

/**
 * Primitive Configuration with a string representation
 * 
 * @author Francois Barre
 * 
 */
public interface PrimitiveConfiguration extends ElementConfiguration
{
    /**
     * @return The String representation
     */
    public String getValue();

    /**
     * @param value
     *            Set the String representation
     */
    public void setValue(String value);
}
