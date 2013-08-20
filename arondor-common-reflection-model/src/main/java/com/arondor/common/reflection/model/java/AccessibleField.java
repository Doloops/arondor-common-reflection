package com.arondor.common.reflection.model.java;

import java.io.Serializable;
import java.util.List;

/**
 * Accessible Field : Model for Java class Field
 * 
 * @author Francois Barre
 * 
 */
public interface AccessibleField extends Serializable
{
    /**
     * @return the field name
     */
    String getName();

    /**
     * 
     * @return the field description
     */
    String getDescription();

    /**
     * 
     * @return the field Class name
     */
    String getClassName();

    void setClassName(String replace);

    List<String> getGenericParameterClassList();

    boolean getReadable();

    boolean getWritable();

    boolean isIs();

}
