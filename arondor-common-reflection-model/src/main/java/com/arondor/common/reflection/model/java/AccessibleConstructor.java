package com.arondor.common.reflection.model.java;

import java.io.Serializable;
import java.util.List;

/**
 * Accessible Constructor : Model for Java class Constructor
 * 
 * @author Francois Barre
 * 
 */
public interface AccessibleConstructor extends Serializable
{
    List<String> getArgumentTypes();
}
