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
    String getName();

    String getDescription();

    String getClassName();

    void setClassName(String replace);

    List<String> getGenericParameterClassList();

    boolean getReadable();

    boolean getWritable();

    boolean isIs();

}
