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

    /**
     * The generic Parameter class list for generics
     * 
     * @return
     */
    List<String> getGenericParameterClassList();

    /**
     * Is the field readable (with a get()) ?
     * 
     * @return true if the field is readable, false otherwise
     */
    boolean getReadable();

    /**
     * Is the field writable (with a set()) ?
     * 
     * @return true if the field is writable, false otherwise
     */
    boolean getWritable();

    /**
     * For booleans, we prefer declaring boolean isField() instead of boolean
     * getField() ; it is a matter of taste actually
     * 
     * @return
     */
    @Deprecated
    boolean isIs();

    /**
     * In which class this field is declared
     */
    String getDeclaredInClass();
}
