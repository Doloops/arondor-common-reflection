package com.arondor.common.reflection.model.java;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Accessible class : Model for Java class
 * 
 * @author Francois Barre
 * 
 */
public interface AccessibleClass extends Serializable
{
    public String getName();

    public String getDescription();

    public String getSuperclass();

    public List<String> getInterfaces();

    public List<String> getAllInterfaces();

    public List<AccessibleConstructor> getConstructors();

    public List<AccessibleMethod> getAccessibleMethods();

    public Map<String, AccessibleField> getAccessibleFields();

    public String getClassBaseName();

    public String getPackageName();

}
